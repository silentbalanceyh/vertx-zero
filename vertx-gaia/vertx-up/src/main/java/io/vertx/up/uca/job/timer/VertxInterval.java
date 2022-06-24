package io.vertx.up.uca.job.timer;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Contract;
import io.vertx.up.experiment.specification.KTimer;

public class VertxInterval implements Interval {
    /*
     * Fix issue of delay < 1ms, the default should be 1
     * Cannot schedule a timer with delay < 1 ms
     */
    private static final int START_UP_MS = 1;

    @Contract
    private transient Vertx vertx;

    @Override
    public Long startAt(final Handler<Long> actuator) {
        return null;
    }

    @Override
    public Long startAt(final Handler<Long> actuator, final KTimer timer) {
        return null;
    }

    @Override
    public Long startAt(final Long delay, final Handler<Long> actuator) {
        return null;
    }

    @Override
    public Long startAt(final Long delay, final Handler<Long> actuator, final KTimer timer) {
        return null;
    }

    @Override
    public long startOldAt(final long delay, final long duration, final Handler<Long> actuator) {
        /*
         * In this kind of situation, only predicate is ok
         * Adjust 10 ms for :
         * -- Cannot schedule a timer with delay < 1 ms
         */
        return this.vertx.setTimer(delay + START_UP_MS, ignored ->
            this.vertx.setPeriodic(START_UP_MS + duration, actuator));
    }

    @Override
    public long startOldAt(final long duration, final Handler<Long> actuator) {
        return this.vertx.setPeriodic(START_UP_MS + duration, actuator);
    }

    @Override
    public long startOldAt(final Handler<Long> handler) {
        /*
         * To avoid booting error, delay set to 3000 ms
         * Instead of start up running
         * Cannot schedule a timer with delay < 1 ms
         */
        return this.vertx.setTimer(START_UP_MS, handler);
    }
}
