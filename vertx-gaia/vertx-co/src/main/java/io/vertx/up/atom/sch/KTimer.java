package io.vertx.up.atom.sch;

import io.horizon.eon.VMessage;
import io.horizon.eon.VValue;
import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * New Object for scheduler information here.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KTimer implements Serializable {
    private static final Annal LOGGER = Annal.get(KTimer.class);
    private final String unique;
    /*
     * duration information of two Job
     * Job1 | ------------ duration -------------| Job2 | ------------- duration -------------
     */
    private TimeUnit durationUnit = TimeUnit.SECONDS;
    /* Default value should be 5 min, here the threshold means seconds */
    private long duration = VValue.RANGE;
    private KTimerFormula formula;

    public KTimer(final String unique) {
        this.unique = unique;
    }

    // -------------------------- Bind -----------------------------

    public KTimer scheduler(final String formula) {
        this.formula = new KTimerFormula(formula, null);
        return this;
    }

    public KTimer scheduler(final String formula, final LocalTime runAt) {
        /* Calculation by runAt */
        if (Objects.isNull(runAt)) {
            Objects.requireNonNull(formula);
            this.formula = new KTimerFormula(formula, null);
        } else {
            /* Formula may be */
            final LocalTime runNow = LocalTime.now();
            // runAt < runNow, day should be adjust for 1
            LocalDate today = LocalDate.now();
            if (runAt.isBefore(runNow)) {
                // Tomorrow
                today = today.plusDays(1);
            }
            final LocalDateTime dateTime = LocalDateTime.of(today, runAt);
            final Instant instant = Ut.parse(dateTime).toInstant();
            this.formula = new KTimerFormula(formula, instant);
        }
        return this;
    }

    /*
     * Based on `duration/unit`, this method will calculate the final duration,
     * here the duration unit is `ms`
     *
     * About `threshold/unit`, the method will calculate the final threshold,
     * here the threshold unit is `nanos`
     */
    public KTimer scheduler(final long duration, final TimeUnit unit) {
        Objects.requireNonNull(unit);
        this.durationUnit = unit;
        this.duration = unit.toMillis(duration);
        return this;
    }

    // -------------------------- Calculation -----------------------------

    public String name() {
        return this.unique;
    }

    public long waitDuration() {
        // Default 5 mins
        if (VValue.RANGE == this.duration) {
            return TimeUnit.MINUTES.toMillis(5);
        } else {
            return this.duration;
        }
    }

    public long waitUntil() {
        final Instant end = this.formula.runAt();
        if (Objects.isNull(end)) {
            /*
             * Fix issue of delay < 1ms, the default should be 1
             * Cannot schedule a timer with delay < 1 ms
             *
             * Type = ONCE
             */
            return 1;
        } else {
            /*
             * Type != ONCE
             */
            final Instant start = Instant.now();
            final long delay = ChronoUnit.MILLIS.between(start, end);
            if (0 < delay) {
                final DateTimeFormatter formatter = this.formula.formatter();
                if (Objects.nonNull(formatter)) {
                    final LocalDateTime datetime = Ut.toDuration(delay);
                    LOGGER.info(VMessage.Job.TIMER.DELAY, this.unique, formatter.format(datetime));
                }
            }
            return delay < 0 ? 1L : delay;
        }
    }

    @Override
    public String toString() {
        return "KTimer{" +
            "unique='" + this.unique + '\'' +
            ", durationUnit=" + this.durationUnit +
            ", duration=" + this.duration +
            ", formula=" + this.formula +
            '}';
    }
}
