package io.vertx.up.uca.job.plan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class JobDaily extends AbstractJobAt {
    /*
     * The time format should be:
     *
     * D,00:08,00:15 ....
     * D,00:12,....
     */
    @Override
    protected LocalDateTime analyze(final LocalDateTime startAt, final LocalTime time, final String segment) {
        // Calculate the New Day
        final LocalDate startDate = startAt.toLocalDate();
        LocalDateTime parsed = LocalDateTime.of(startDate, time);
        // Here the segment is null
        if (parsed.isBefore(startAt)) {
            parsed = parsed.plusDays(1L);
        }
        return parsed;
    }

    @Override
    public String format() {
        return "'Time='HH:mm:ss.SSS";
    }
}
