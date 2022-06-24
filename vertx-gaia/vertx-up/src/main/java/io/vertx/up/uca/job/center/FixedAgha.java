package io.vertx.up.uca.job.center;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.tp.plugin.job.JobClient;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.eon.Info;
import io.vertx.up.experiment.specification.KTimer;
import io.vertx.up.log.Debugger;
import io.vertx.up.util.Ut;

import java.util.Objects;

class FixedAgha extends AbstractAgha {

    @Override
    public Future<Long> begin(final Mission mission) {
        /*
         * Calculate started for delay output
         */
        final KTimer timer = mission.timer();
        Objects.requireNonNull(timer);

        final long delay = timer.delay();
        final long duration = timer.duration();
        // final long delay = this.getDelay(mission);
        /*
         * STARTING -> READY
         **/
        this.moveOn(mission, true);

        final Promise<Long> future = Promise.promise();
        final long jobId = this.interval().startAt(delay, duration, (timeId) -> this.working(mission, () -> {
            /*
             * Complete future and returned Async
             */
            future.tryComplete(timeId);
            /*
             * RUNNING -> STOPPED -> READY
             */
            Ut.itRepeat(2, () -> this.moveOn(mission, true));
        }));
        JobClient.bind(jobId, mission.getCode());
        if (Debugger.onJobBooting()) {
            this.getLogger().info(Info.JOB_INTERVAL, mission.getCode(),
                String.valueOf(delay), String.valueOf(duration), String.valueOf(jobId));
        }
        return future.future();
    }
}
