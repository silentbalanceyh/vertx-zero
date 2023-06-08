package io.vertx.mod.jet.atom;

import cn.vertxup.jet.domain.tables.pojos.IJob;
import cn.vertxup.jet.domain.tables.pojos.IService;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.jet.cv.JtKey;
import io.vertx.mod.jet.refine.Jt;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.atom.sch.KTimer;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.eon.em.EmJob;
import io.vertx.up.util.Ut;

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
        return Jt.toOptions(this.service(), this.job);
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
        mission.setType(Ut.toEnum(this.job::getType, EmJob.JobType.class, EmJob.JobType.ONCE));
        mission.setCode(Jt.jobCode(this.job));
        mission.setReadOnly(Boolean.FALSE);
        /* Basic information */
        mission.setComment(this.job.getComment());
        mission.setAdditional(Ut.toJObject(this.job.getAdditional()));
        /* Set job configuration of current environment. bind to `service` */
        mission.setMetadata(this.toJson().copy());


        /*
         * EmApp `name` missing in future
         * JtApp processing
         */
        final IService service = this.service();
        final HArk ark = Ke.ark(service.getSigma());
        mission.ark(ark);
        mission.timeout(this.job.getThreshold(), TimeUnit.MINUTES);

        this.setTimer(mission);
        /*
         * Component executor here.
         * KIncome / incomeAddress
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

    private void setTimer(final Mission mission) {
        /*
         * Scheduler of Mission definition here and you can set any information
         * of current Part.
         */
        final KTimer timer = new KTimer(mission.getCode());
        final String runFormula = this.job.getRunFormula();
        // Error-60054 Detect
        mission.detectPre(runFormula);
        final EmJob.JobType type = mission.getType();
        if (EmJob.JobType.ONCE != type) {
            timer.scheduler(runFormula, this.job.getRunAt());
            if (Objects.nonNull(this.job.getDuration())) {
                timer.scheduler(this.job.getDuration(), TimeUnit.MINUTES);
            }
            mission.timer(timer);
        }
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
