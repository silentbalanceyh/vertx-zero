package io.vertx.up.uca.job.store;

import io.vertx.up.atom.worker.Mission;

import java.util.Set;

/*
 * Package using here for job reading
 */
interface JobReader {

    /*
     * Get all job definition from zero framework
     */
    Set<Mission> fetch();

    /*
     * Find job by code.
     */
    Mission fetch(String code);
}
