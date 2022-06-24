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
import java.util.ArrayList;
import java.util.List;
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
     * View based on FIXED ( Plan ) Here.
     * FIXED ->
     *       -> Time = runAt
     *       -> Duration = ( ms ) The system unit of FIXED, it's common scheduler.
     *
     * ( Width is Fixed )
     * DAY   -> Time = runAt
     *       -> Duration = ( day ) The system unit will convert `day -> ms`.
     * WEEK  -> Time = runAt
     *       -> Duration = ( week ) The system unit will convert `week -> ms`.
     *
     * ( Width is Non Fixed )
     * MONTH    -> Time = runAt
     *          -> Duration = ( Invalid )
     *          -> viewAt = 1 ~ 28/29/30/31 etc.
     * QUARTER  -> Time = runAt
     *          -> Duration = ( Invalid )
     *          -> viewAt = [1/2/3, 1 ~ 28/29/30/31] etc.
     * YEAR     -> Time = runAt
     *          -> Duration = ( Invalid )
     *          -> viewAt = [1 ~ 12, 1 ~ 28/29/30/31] etc.
     */
    private final List<Integer> viewAt = new ArrayList<>();
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
    public KTimer waitFor(final Instant runAt) {
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

    // -------------------------- Calculation -----------------------------

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
            ", durationUnit=" + this.durationUnit +
            ", duration=" + this.duration +
            ", runAt=" + this.runAt +
            '}';
    }
}
