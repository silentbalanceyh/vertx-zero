package io.vertx.up.uca.job.timer;

import io.horizon.eon.VMessage;
import io.horizon.uca.log.Annal;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Contract;
import io.vertx.up.atom.sch.KTimer;
import io.vertx.up.util.Ut;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Consumer;

public class VertxInterval implements Interval {
    private static final Annal LOGGER = Annal.get(VertxInterval.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd HH:mm.ss.SSS");
    /*
     * Fix issue of delay < 1ms, the default should be 1
     * Cannot schedule a timer with delay < 1 ms
     */
    private static final int START_UP_MS = 1;

    @Contract
    private transient Vertx vertx;

    private Consumer<Long> controlFn;

    @Override
    public Interval bind(final Consumer<Long> controlFn) {
        this.controlFn = controlFn;
        return this;
    }

    /*
     * This situation is for executing without any `delay` part
     * here, although you have set the `delay` parameter in `Timer` here
     * but the code logical will ignore `delay`.
     *
     * For `delay` part the system should avoid following issue:
     * Fix issue of delay < 1ms, the default should be 1, Cannot schedule a timer with delay < 1 ms
     *
     * The 1ms is started. When following condition has been triggered, here are two code logical
     *
     * 1) KTimer is null        ( Once Job )
     * 2) KTimer is not null    ( Legacy Plan Job )
     */
    @Override
    public void startAt(final Handler<Long> actuator, final KTimer timer) {
        if (Objects.isNull(timer)) {
            /*
             * Because timer is null, delay ms is not needed
             * In this kind of situation
             * call vertx.setTimer only, the smallest is 1ms ( Right Now )
             */
            LOGGER.info(VMessage.Job.INTERVAL.START);
            this.vertx.setTimer(START_UP_MS, actuator);
        } else {
            /*
             * Extract delay ms from `timer` reference
             * Be careful about the timerId here, the returned timerId
             *
             * Here are two timerId
             * 1. setTimer          ( Returned Directly )
             * 2. setPeriodic       ( Output by actuator )
             */
            final long waitSec = timer.waitUntil();
            final long delay = waitSec + START_UP_MS;
            this.vertx.setTimer(delay, ignored -> {
                final long duration = timer.waitDuration() + START_UP_MS;
                final long timerId = this.vertx.setPeriodic(duration, actuator);
                /*
                 * Bind the controlFn to consume the timerId of periodic timer
                 * In the document of vert.x here are comments:
                 *
                 * To cancel a periodic timer, call cancelTimer specifying the timer id. For example:
                 * vertx.cancelTimer(timerID);
                 */
                LOGGER.info(VMessage.Job.INTERVAL.SCHEDULED, String.valueOf(timerId), timer.name(), duration);
                if (Objects.nonNull(this.controlFn)) {
                    this.controlFn.accept(timerId);
                }
            });
            LOGGER.info(VMessage.Job.INTERVAL.DELAY_START, timer.name(), FORMATTER.format(Ut.toDuration(waitSec)));
        }
    }

    @Override
    public void restartAt(final Handler<Long> actuator, final KTimer timer) {
        if (Objects.isNull(timer)) {
            LOGGER.info(VMessage.Job.INTERVAL.RESTART);
            this.vertx.setTimer(START_UP_MS, actuator);
        } else {
            final long waitSec = timer.waitUntil();
            final long delay = waitSec + START_UP_MS;
            this.vertx.setTimer(delay, actuator);
            LOGGER.info(VMessage.Job.INTERVAL.DELAY_RESTART, timer.name(), FORMATTER.format(Ut.toDuration(waitSec)));
        }
    }
}
