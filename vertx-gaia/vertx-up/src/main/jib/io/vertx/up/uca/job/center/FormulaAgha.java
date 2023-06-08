package io.vertx.up.uca.job.center;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.up.atom.sch.KTimer;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.eon.em.EmJob;
import io.vertx.up.uca.job.timer.Interval;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FormulaAgha extends AbstractAgha {
    @Override
    public Future<Long> begin(final Mission mission) {
        // STARTING -> READY
        this.moveOn(mission, true);

        return this.execute(mission);
    }

    private Future<Long> execute(final Mission mission) {
        final Promise<Long> promise = Promise.promise();
        final Interval interval = this.interval();
        final KTimer timer = mission.timer();
        interval.restartAt((timeId) -> {
            // STOPPED -> READY
            if (EmJob.Status.STOPPED == mission.getStatus()) {
                this.moveOn(mission, true);
            }
            this.working(mission, () -> {
                /*
                 * Complete future and returned Async
                 */
                promise.tryComplete(timeId);

                // RUNNING -> STOPPED
                this.moveOn(mission, true);
            });
        }, timer);
        return promise.future()
            /*
             * Call internal execute in loop because of
             * continue working on
             */
            .compose(finished -> this.execute(mission));
    }
}
