package io.vertx.up.experiment.specification;

import io.vertx.up.eon.Info;
import io.vertx.up.eon.Values;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
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
     * threshold information of one Job
     * Job Information of timeout Part
     */
    private TimeUnit thresholdUnit = TimeUnit.SECONDS;
    /* Default value should be 15 min, here the threshold means nanos */
    private long threshold = Values.RANGE;
    /*
     * duration information of two Job
     * Job1 | ------------ duration -------------| Job2 | ------------- duration -------------
     */
    private TimeUnit durationUnit = TimeUnit.SECONDS;
    /* Default value should be 5 min, here the threshold means seconds */
    private long duration = Values.RANGE;
    /*
     * Job Type for next time runAt Part
     */
    private Instant runAt;

    public KTimer(final String unique) {
        this.unique = unique;
    }

    // -------------------------- Bind -----------------------------
    public KTimer scheduler(final Instant runAt) {
        this.runAt = runAt;
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

    public KTimer timeout(final long threshold, final TimeUnit unit) {
        this.threshold = unit.toNanos(threshold);
        this.thresholdUnit = unit;
        return this;
    }

    /*
     * Fast setting based on `seconds` that stored into `I_JOB`
     */
    public KTimer scheduler(final Long duration) {
        if (Objects.isNull(duration)) {
            this.duration = Values.RANGE;
            return this;
        } else {
            // Seconds -> Mill Seconds
            return this.scheduler(duration, this.durationUnit);
        }
    }

    public KTimer timeout(final Integer threshold) {
        if (Objects.isNull(threshold)) {
            this.threshold = Values.RANGE;
            return this;
        } else {
            // Seconds -> Nano Seconds
            return this.timeout(threshold, this.thresholdUnit);
        }
    }

    // -------------------------- Calculation -----------------------------

    public long threshold() {
        // Default 15 mins
        if (Values.RANGE == this.threshold) {
            return TimeUnit.MINUTES.toNanos(15);
        } else {
            return this.threshold;
        }
    }

    public long duration() {
        // Default 5 mins
        if (Values.RANGE == this.duration) {
            return TimeUnit.MINUTES.toMillis(5);
        } else {
            return this.duration;
        }
    }

    // -------------------------- Future At ------------------------------
    public long delay() {
        Objects.requireNonNull(this.runAt);
        final Instant end = this.runAt;
        final Instant start = Instant.now();

        final long delay = ChronoUnit.MILLIS.between(start, end);
        if (0 < delay) {
            final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            final LocalDateTime datetime = Ut.toDuration(delay);
            LOGGER.info(Info.JOB_DELAY, this.unique, format.format(datetime));
        }
        return delay < 0 ? 0L : delay;
    }

    @Override
    public String toString() {
        return "KTimer{" +
            "unique='" + this.unique + '\'' +
            ", thresholdUnit=" + this.thresholdUnit +
            ", threshold=" + this.threshold +
            ", durationUnit=" + this.durationUnit +
            ", duration=" + this.duration +
            ", runAt=" + this.runAt +
            '}';
    }
}
