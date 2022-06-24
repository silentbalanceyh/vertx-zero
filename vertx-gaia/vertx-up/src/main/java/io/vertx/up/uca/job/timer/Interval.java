package io.vertx.up.uca.job.timer;

import io.vertx.core.Handler;
import io.vertx.up.experiment.specification.KTimer;

/*
 * Scheduled for each
 */
public interface Interval {
    /*
     * New design for job extension interval scheduler management the schedule instead of
     * original three:
     *
     * - FIXED
     * - PLAN
     * - ONCE
     *
     * The extension are as following:
     * - MONTH
     * - WEEK
     * - QUARTER
     * - YEAR
     *
     * Here are normalized phase:
     * 1) Wait
     * 2) Run
     * 3) Repeat Or End
     * 4) Update KTimer `runAt` of next time
     */

    /**
     * --- No Wait ------ >>> ------- End
     *
     * 「Development」
     * This method call directly and it's for development often, after the server get
     * the commend from front-end user interface, the Job start right now. it means that when
     * the developer want to debug the job detail from user interface, this api could be
     * called to see the job running details.
     *
     * 1.
     *
     * @param actuator Executor
     *
     * @return TimerId
     */
    Long startAt(Handler<Long> actuator);

    Long startAt(Handler<Long> actuator, KTimer timer);

    Long startAt(Long delay, Handler<Long> actuator);

    Long startAt(Long delay, Handler<Long> actuator, KTimer timer);

    /**
     * Start schedule at
     *
     * @param delay    delay ms to begin
     * @param duration repeat for each duration
     * @param actuator Executor
     */
    long startOldAt(long delay, long duration, Handler<Long> actuator);

    /**
     * Start schedule from now without delay
     *
     * @param duration repeat for each duration
     * @param actuator Executor
     */
    long startOldAt(long duration, Handler<Long> actuator);

    /**
     * Start schedule once
     *
     * @param actuator Executor
     */
    long startOldAt(Handler<Long> actuator);
}
