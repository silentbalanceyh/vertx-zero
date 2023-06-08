package io.vertx.up.uca.job.store;

import io.vertx.up.atom.worker.Mission;

/**
 * JobStore bridge for Set<Mission> get
 * 1) @Job annotation class here
 * 2) Database job store here that configured in vertx-job.yml
 */
public interface JobStore extends JobReader {

    /*
     * Remove mission from store
     */
    JobStore remove(Mission mission);

    /*
     * Update mission in store
     */
    JobStore update(Mission mission);

    /*
     * Add new mission into Set<Mission>
     */
    JobStore add(Mission mission);
}
