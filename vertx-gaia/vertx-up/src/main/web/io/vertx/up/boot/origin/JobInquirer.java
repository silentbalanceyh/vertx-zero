package io.vertx.up.boot.origin;

import io.horizon.eon.VMessage;
import io.horizon.uca.log.Annal;
import io.vertx.up.annotations.Job;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.backbone.Extractor;
import io.vertx.up.backbone.config.JobExtractor;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class JobInquirer implements Inquirer<Set<Mission>> {

    private static final Annal LOGGER = Annal.get(JobInquirer.class);

    private final transient Extractor<Mission> extractor = Ut.singleton(JobExtractor.class);

    @Override
    public Set<Mission> scan(final Set<Class<?>> clazzes) {
        final Set<Class<?>> jobs = clazzes.stream()
            .filter(item -> item.isAnnotationPresent(Job.class))
            .collect(Collectors.toSet());
        /* All classes of jobs here */
        LOGGER.info(VMessage.Inquirer.JOB, jobs.size());
        return jobs.stream().map(this.extractor::extract)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }
}
