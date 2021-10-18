package io.vertx.tp.jet.atom;

import cn.vertxup.jet.domain.tables.pojos.IJob;
import cn.vertxup.jet.domain.tables.pojos.IService;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.cv.JtKey;
import io.vertx.tp.jet.refine.Jt;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.JobType;
import io.vertx.up.util.Ut;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/*
 * Job ( JOB + SERVICE )
 */
public class JtJob extends JtCommercial {

    private transient IJob job;
    private transient String key;

    /*
     * For deserialization
     */
    public JtJob() {
    }

    public JtJob(final IJob job, final IService service) {
        super(service);
        this.job = job;
        /* */
        this.key = job.getKey();
    }
    // ----------- override

    @Override
    public JsonObject options() {
        return Jt.toOptions(this.getApp(), this.job, this.service());
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public JsonObject toJson() {
        final JsonObject data = super.toJson();
        /* key data */
        data.put(JtKey.Delivery.JOB, Ut.serializeJson(this.job));
        return data;
    }

    @Override
    public void fromJson(final JsonObject data) {
        super.fromJson(data);
        /*
         * Basic attributes
         */
        this.key = data.getString(JtKey.Delivery.KEY);
        /*
         * job
         */
        this.job = Ut.deserialize(data.getJsonObject(JtKey.Delivery.JOB), IJob.class);
    }

    // ----------- job & service

    public Mission toJob() {
        final Mission mission = new Mission();
        /*
         * IJob -> Mission：code
         * 1) Job alias is job name for standalone here
         * 2) Job code/name must be `namespace + code` to build unique identifier of current job
         * 3) Default job type is ONCE
         * 4) For job configuration, it's different for
         * - 4.1) All the programming job should be `READONLY` ( hard coding )
         * - 4.2) All the extension job ( stored into database ) should be `EDITABLE` ( dynamic )
         */
        mission.setName(this.job.getName());
        mission.setType(Ut.toEnum(this.job::getType, JobType.class, JobType.ONCE));
        mission.setCode(Jt.jobCode(this.job));
        mission.setReadOnly(Boolean.FALSE);
        /*
         * Basic information
         */
        mission.setComment(this.job.getComment());
        mission.setAdditional(Ut.toJObject(this.job.getAdditional()));
        /*
         * Set job configuration of current environment. bind to `service`
         */
        mission.setMetadata(this.toJson().copy());
        /*
         * Instant / duration
         */
        if (Objects.nonNull(this.job.getRunAt())) {
            final LocalTime runAt = this.job.getRunAt();
            final LocalTime runNow = LocalTime.now();
            /*
             * Whether adjust 1 day plus
             */
            LocalDate today = LocalDate.now();
            if (runAt.isBefore(runNow)) {
                today = today.plusDays(1);
            }
            /*
             * Final LocalTime calculation
             */
            final LocalDateTime result = LocalDateTime.of(today, runAt);
            mission.setInstant(Ut.parse(result).toInstant());
        }
        /*
         * Proxy & @On @Off without @Job method
         */
        if (Objects.isNull(this.job.getDuration())) {
            mission.setDuration(Values.RANGE);
        } else {
            final long duration = TimeUnit.SECONDS.toMillis(this.job.getDuration());
            mission.setDuration(duration);
        }
        if (Objects.isNull(this.job.getThreshold())) {
            mission.setThreshold(Values.RANGE);
        } else {
            final long threshold = TimeUnit.SECONDS.toNanos(this.job.getThreshold());
            mission.setThreshold(threshold);
        }
        /*
         * Income / incomeAddress
         */
        if (Objects.nonNull(this.job.getIncomeComponent())) {
            mission.setIncome(Ut.clazz(this.job.getIncomeComponent()));
        }
        mission.setIncomeAddress(this.job.getIncomeAddress());
        /*
         * Outcome / outcomeAddress
         */
        if (Objects.nonNull(this.job.getOutcomeComponent())) {
            mission.setOutcome(Ut.clazz(this.job.getOutcomeComponent()));
        }
        mission.setOutcomeAddress(this.job.getOutcomeAddress());
        return this.mount(mission);
    }

    private Mission mount(final Mission mission) {
        final String proxyStr = this.job.getProxy();
        final Class<?> clazz = Ut.clazz(proxyStr);
        if (Objects.nonNull(clazz)) {
            /*
             * Object initialized
             */
            return mission.connect(clazz);
        } else {
            return mission;
        }
    }
}
