package io.vertx.up.backbone.config;

import io.horizon.eon.VMessage;
import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Job;
import io.vertx.up.atom.sch.KTimer;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.backbone.Extractor;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import io.vertx.up.eon.em.EmJob;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

;

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
        final EmJob.JobType type = Ut.invoke(annotation, KName.VALUE);

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
        mission.setStatus(EmJob.Status.STARTING);

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
            mission.setCode(KWeb.JOB.NS + VString.DASH + mission.getName());
        }
        mission.connect(clazz);
        /* on method must existing */
        if (Objects.isNull(mission.getOn())) {
            LOGGER.warn(VMessage.Extractor.JOB_IGNORE, clazz.getName());
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
        if (Ut.isNotNil(config)) {
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
        final StringBuilder file = new StringBuilder(KWeb.JOB.PREFIX);
        if (config.startsWith(VString.SLASH)) {
            /* config contains `/` prefix */
            file.append(config);
        } else {
            file.append(VString.SLASH).append(config);
        }
        if (!config.endsWith(VString.DOT + VPath.SUFFIX.JSON)) {
            file.append(VString.DOT).append(VPath.SUFFIX.JSON);
        }
        return file.toString().replace("//", "/");
    }
}
