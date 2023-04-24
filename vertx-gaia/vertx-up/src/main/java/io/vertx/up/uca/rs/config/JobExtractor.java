package io.vertx.up.uca.rs.config;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Job;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.eon.Info;
import io.vertx.up.eon.*;
import io.vertx.up.eon.em.JobStatus;
import io.vertx.up.eon.em.JobType;
import io.aeon.experiment.specification.sch.KTimer;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.rs.Extractor;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class JobExtractor implements Extractor<Mission> {

    private static final Annal LOGGER = Annal.get(JobExtractor.class);

    @Override
    public Mission extract(final Class<?> clazz) {
        /*
         * Mission initializing
         */
        final Annotation annotation = clazz.getAnnotation(Job.class);
        if (Objects.isNull(annotation)) {
            /*
             * If Job annotation could not get, return null;
             */
            return null;
        }
        /* Default type */
        final JobType type = Ut.invoke(annotation, KName.VALUE);

        /* Default name -> class name */
        String name = Ut.invoke(annotation, KName.NAME);
        name = Ut.isNil(name) ? clazz.getName() : name;

        /* Initialization */
        final Mission mission = this.config(annotation);
        /*
         * Basic data object initialized
         * For this kind of situation, the job name should be equal to alias
         * */
        mission.setName(name);
        mission.setReadOnly(Boolean.TRUE);

        /*
         * Let type could be configured,
         * 1) Annotation type priority should be low
         * 2) Config type priority is higher than annotation
         */
        if (Objects.isNull(mission.getType())) {
            mission.setType(type);
        }

        /* The first status of each Job */
        mission.setStatus(JobStatus.STARTING);

        {
            /* threshold / thresholdUnit */
            final TimeUnit thresholdUnit = Ut.invoke(annotation, "thresholdUnit");
            final Integer threshold = Ut.invoke(annotation, "threshold");
            // threshold = thresholdUnit.toNanos(threshold);
            mission.timeout(threshold, thresholdUnit);
        }
        /* Set Timer */
        this.setTimer(mission, annotation);

        /* code sync */
        if (Ut.isNil(mission.getCode())) {
            mission.setCode(Constants.DEFAULT_JOB_NAMESPACE + Strings.DASH + mission.getName());
        }
        mission.connect(clazz);
        /* on method must existing */
        if (Objects.isNull(mission.getOn())) {
            LOGGER.warn(Info.JOB_IGNORE, clazz.getName());
            return null;
        }
        return mission;
    }

    private void setTimer(final Mission mission, final Annotation annotation) {
        /* Timer of Mission Building */
        final KTimer timer = new KTimer(mission.getCode());
        {
            /* duration / durationUnit */
            final TimeUnit durationUnit = Ut.invoke(annotation, "durationUnit");
            final long duration = Ut.invoke(annotation, "duration");
            // duration = durationUnit.toMillis(duration);
            timer.scheduler(duration, durationUnit);
        }
        /* formula calculate */
        final String runFormula = Ut.invoke(annotation, "formula");
        // Error-60054 Detect
        mission.detectPre(runFormula);
        timer.scheduler(runFormula, null);
        mission.timer(timer);
    }

    private Mission config(final Annotation annotation) {
        /* Config */
        final String config = Ut.invoke(annotation, KName.CONFIG);
        final Mission mission;
        if (Ut.notNil(config)) {
            final JsonObject json = Ut.ioJObject(this.resolve(config));
            /*
             * Removed
             * - status
             * - name
             * - type
             * Be carefule, here include
             * - income
             * - incomeAddress
             * - outcome
             * - outcomeAddress
             * */
            json.remove(KName.STATUS);
            json.remove(KName.NAME);
            json.remove(KName.TYPE);
            json.remove("instant");
            mission = Ut.deserialize(json, Mission.class);
        } else {
            mission = new Mission();
        }
        return mission;
    }

    private String resolve(final String config) {
        final StringBuilder file = new StringBuilder(Constants.DEFAULT_JOB);
        if (config.startsWith(Strings.SLASH)) {
            /* config contains `/` prefix */
            file.append(config);
        } else {
            file.append(Strings.SLASH).append(config);
        }
        if (!config.endsWith(Strings.DOT + FileSuffix.JSON)) {
            file.append(Strings.DOT).append(FileSuffix.JSON);
        }
        return file.toString().replace("//", "/");
    }
}
