package io.vertx.up.uca.job.plan;

import io.vertx.up.atom.Kv;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractJobAt implements JobAt {
    @Override
    public Queue<Instant> analyze(final List<String> formulas, final Instant instant) {
        final List<Instant> parsedList = new ArrayList<>();
        formulas.forEach(formula -> {
            final String[] segments = formula.split(Strings.SLASH);
            if (1 <= segments.length) {
                final LocalTime time = Ut.toTime(segments[0]);
                // Extract Segment of Part 2
                final String segment;
                if (1 == segments.length) {
                    segment = null;
                } else {
                    segment = segments[1];
                }
                /*
                 * Child Analyzing Based on:
                 * 1. LocalTime
                 * 2. Segment
                 * 3. The TimeStamp Of Each Start
                 */
                final LocalDateTime startAt = Ut.toDateTime(instant);
                final LocalDateTime parsedAt = this.analyze(startAt, time, segment);
                if (Objects.nonNull(parsedAt)) {
                    final Instant parsed = Ut.parse(parsedAt).toInstant();
                    if (parsed.isAfter(instant)) {
                        /*
                         * parsed > instant ( Valid )
                         **/
                        parsedList.add(parsed);
                    }
                }
            } else {
                this.logger().warn("The formula could not be parsed: {0}", formula);
            }
        });
        // Instant from `past -> now -> future`
        parsedList.sort(Instant::compareTo);
        return new ConcurrentLinkedDeque<>(parsedList);
    }

    /*
     * When the duration time is greater than 1 day:
     * yyyy - years
     * MM   - months
     * dd   - days
     *
     * The left part is
     * HH   - hours
     * mm   - minutes
     * ss   - seconds
     * SSS  - mill-seconds
     */

    protected LocalDateTime analyze(final LocalDateTime startAt, final LocalTime time, final String segment) {
        throw new _501NotSupportException(this.getClass());
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    private Kv<Integer, Integer> parseSegment(final String segment) {
        if (Ut.notNil(segment) && segment.contains(Strings.DASH)) {
            try {
                final String[] split = segment.split(Strings.DASH);
                final Integer m = Integer.parseInt(split[0]);
                final Integer d = Integer.parseInt(split[1]);
                return Kv.create(m, d);
            } catch (final Throwable ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    protected LocalDateTime plusWith(final LocalDateTime find, final String segment) {
        final Kv<Integer, Integer> md = this.parseSegment(segment);// Calculate the New Day
        if (Objects.isNull(md)) {
            final int dayAdjust = Integer.parseInt(segment);
            return find.plusDays(dayAdjust - 1);
        } else {
            return find.plusMonths(md.getKey() - 1)
                .plusDays(md.getValue() - 1);
        }
    }
}
