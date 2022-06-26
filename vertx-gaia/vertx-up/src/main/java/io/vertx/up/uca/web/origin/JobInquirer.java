package io.vertx.up.uca.web.origin;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Job;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Info;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.JobStatus;
import io.vertx.up.eon.em.JobType;
import io.vertx.up.experiment.specification.sch.KTimer;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class JobInquirer implements Inquirer<Set<Mission>> {

    private static final Annal LOGGER = Annal.get(JobInquirer.class);

    @Override
    public Set<Mission> scan(final Set<Class<?>> clazzes) {
        final Set<Class<?>> jobs = clazzes.stream()
            .filter(item -> item.isAnnotationPresent(Job.class))
            .collect(Collectors.toSet());
        /* All classes of jobs here */
        LOGGER.info(Info.SCANED_JOB, jobs.size());
        return jobs.stream().map(this::initialize)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    private Mission initialize(final Class<?> clazz) {
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
        final JobType type = Ut.invoke(annotation, "value");

        /* Default name -> class name */
        String name = Ut.invoke(annotation, "name");
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
        // Error-40078 Detect
        mission.detectPre(runFormula);
        timer.scheduler(runFormula, null);
        mission.timer(timer);
    }

    private Mission config(final Annotation annotation) {
        /* Config */
        final String config = Ut.invoke(annotation, "config");
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
            json.remove("status");
            json.remove("name");
            json.remove("type");
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
