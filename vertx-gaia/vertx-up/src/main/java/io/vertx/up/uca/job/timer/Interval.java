package io.vertx.up.uca.job.timer;

import io.vertx.core.Handler;

/*
 * Scheduled for each
 */
public interface Interval {
    /**
     * Start schedule at
     *
     * @param delay    delay ms to begin
     * @param duration repeat for each duration
     * @param actuator Executor
     */
    long startAt(long delay, long duration, Handler<Long> actuator);

    /**
     * Start schedule from now without delay
     *
     * @param duration repeat for each duration
     * @param actuator Executor
     */
    long startAt(long duration, Handler<Long> actuator);

    /**
     * Start schedule once
     *
     * @param actuator Executor
     */
    long startAt(Handler<Long> actuator);
}
