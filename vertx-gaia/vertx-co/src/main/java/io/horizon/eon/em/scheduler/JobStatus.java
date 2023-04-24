package io.horizon.eon.em.scheduler;

/*
 * State machine moving:
 * STARTING ------|
 *                v
 *     |------> READY <-------------------|
 *     |          |                       |
 *     |          |                     <start>
 *     |          |                       |
 *     |        <start>                   |
 *     |          |                       |
 *     |          V                       |
 *     |        RUNNING --- <stop> ---> STOPPED
 *     |          |
 *     |          |
 * <resume>   ( error )
 *     |          |
 *     |          |
 *     |          v
 *     |------- ERROR
 *
 * 1) The first time when worker initialized the job, job status will be READY
 *    -- in this situation, the first time will be different when status moving
 * 2) For ONCE, FIXED, the job will not run when STARTING
 *    -- 2.1. ONCE job must be triggered by event;
 *    -- 2.2. FIXED job must run after delay xxx duration;
 * 3) For PLAN, the job will run when STARTING, because it will repeat duration sometime
 */
public enum JobStatus {
    STARTING,
    READY,      // The job could be started
    RUNNING,        // Job is running
    STOPPED,    // Job is stopped
    ERROR,      // Job met error when ran last time
}
