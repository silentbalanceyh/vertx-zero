package io.vertx.up.uca.job.center;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.tp.plugin.job.JobClient;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.eon.Info;
import io.vertx.up.log.Debugger;
import io.vertx.up.util.Ut;

class PlanAgha extends AbstractAgha {

    @Override
    public Future<Long> begin(final Mission mission) {
        final Promise<Long> future = Promise.promise();
        /*
         * STARTING -> READY
         **/
        this.moveOn(mission, true);
        final long jobId = this.interval().startAt(mission.getDuration(), (timeId) -> this.working(mission, () -> {
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
        if (Debugger.onJooqCondition()) {
            this.getLogger().info(Info.JOB_INTERVAL, mission.getCode(),
                    String.valueOf(0), String.valueOf(mission.getDuration()), String.valueOf(jobId));
        }
        return future.future();
    }
}
