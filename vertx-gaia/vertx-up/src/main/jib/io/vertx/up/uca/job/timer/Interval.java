package io.vertx.up.uca.job.timer;

import io.vertx.core.Handler;
import io.vertx.up.atom.sch.KTimer;

import java.util.function.Consumer;

/*
 * Scheduled for each
 */
public interface Interval {

    Interval bind(Consumer<Long> controlFn);
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
     * @param actuator Executor
     */
    default void startAt(final Handler<Long> actuator) {
        this.startAt(actuator, null);
    }

    void startAt(Handler<Long> actuator, KTimer timer);

    void restartAt(Handler<Long> actuator, KTimer timer);
}
