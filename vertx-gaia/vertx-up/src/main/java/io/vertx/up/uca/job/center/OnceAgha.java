package io.vertx.up.uca.job.center;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.uca.job.timer.Interval;

/**
 * Start one time
 */
class OnceAgha extends AbstractAgha {

    @Override
    public Future<Long> begin(final Mission mission) {
        /*
         * 1. Execute this mission directly
         * -  This category could not be started when worker deployed, instead, this Agha should
         *    check whether this task is in `JobPool` because it will be triggered in future.
         * 2. This task is only once execution, invoke `@On / @Off` at the same time.
         * 3. This kind fo task must be triggered, could not be in plan here. It's not needed to call
         *    Interval to process task.
         * */

        // STARTING -> READY
        this.moveOn(mission, true);

        final Promise<Long> promise = Promise.promise();
        final Interval interval = this.interval();
        interval.startAt((timeId) -> this.working(mission, () -> {
            /*
             * Complete future and returned Async
             */
            promise.tryComplete(timeId);

            // RUNNING -> STOPPED
            this.moveOn(mission, true);
        }));
        return promise.future();
    }
}
