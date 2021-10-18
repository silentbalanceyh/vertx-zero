package io.vertx.tp.jet.uca.micro;

import io.vertx.tp.jet.atom.JtJob;
import io.vertx.tp.optic.environment.Ambient;
import io.vertx.tp.optic.environment.AmbientEnvironment;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.uca.job.store.JobStore;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 「JobStore」
 * Database job store that will be used in `vertx-jet`.
 */
public class JtHypnos implements JobStore {

    private static final ConcurrentMap<String, AmbientEnvironment> ENVS =
        Ambient.getEnvironments();

    @Override
    public JobStore remove(final Mission mission) {
        // TODO: Remove in future
        return null;
    }

    @Override
    public JobStore update(final Mission mission) {
        // TODO: Update in future
        return null;
    }

    @Override
    public JobStore add(final Mission mission) {
        // TODO: Add in future
        return null;
    }

    @Override
    public Mission fetch(final String name) {
        // TODO: Fetch in future
        return null;
    }

    @Override
    public Set<Mission> fetch() {
        return ENVS.values().stream().filter(Objects::nonNull)
            .flatMap(environment -> environment.jobs().stream())
            .map(JtJob::toJob)
            .collect(Collectors.toSet());
    }
}
