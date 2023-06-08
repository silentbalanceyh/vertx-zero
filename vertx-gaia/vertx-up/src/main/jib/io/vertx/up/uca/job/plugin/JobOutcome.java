package io.vertx.up.uca.job.plugin;

import io.vertx.core.Future;
import io.vertx.up.commune.Envelop;

/*
 * Job outcome, this outcome interface should provide Future<JobOut> to execute
 */
public interface JobOutcome {
    /*
     * Async process outcome here
     */
    Future<Envelop> afterAsync(final Envelop envelop);
}
