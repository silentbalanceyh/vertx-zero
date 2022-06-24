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

class PlanAgha extends AbstractAgha {

    @Override
    public Future<Long> begin(final Mission mission) {
        final Promise<Long> future = Promise.promise();
        final KTimer timer = mission.timer();
        Objects.requireNonNull(timer);
        final long duration = timer.duration();
        /*
         * STARTING -> READY
         **/
        this.moveOn(mission, true);
        final long jobId = this.interval().startAt(duration, (timeId) -> this.working(mission, () -> {
            /*fd
             * Complete future and returned Async
             */
            future.tryComplete(timeId);
            /*d
             * RUNNING -> STOPPED -> READY
             */
            Ut.itRepeat(2, () -> this.moveOn(mission, true));
        }));
        JobClient.bind(jobId, mission.getCode());
        if (Debugger.onJobBooting()) {
            this.getLogger().info(Info.JOB_INTERVAL, mission.getCode(),
                String.valueOf(0), String.valueOf(duration), String.valueOf(jobId));
        }
        return future.future();
    }
}
