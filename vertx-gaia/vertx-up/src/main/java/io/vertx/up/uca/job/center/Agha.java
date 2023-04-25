package io.vertx.up.uca.job.center;

import io.horizon.eon.em.scheduler.JobType;
import io.vertx.core.Future;
import io.vertx.up.atom.worker.Mission;

/**
 * Job manager to manage each job here.
 */
public interface Agha {

    static Agha get(final JobType type) {
        return Pool.AGHAS.getOrDefault(type, new OnceAgha());
    }

    /**
     * Start new job by definition of Mission here.
     * Async start and return Future<Long>,
     * here long type is timerId, you can control this job by timerId
     */
    Future<Long> begin(Mission mission);
}
