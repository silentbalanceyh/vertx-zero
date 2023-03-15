package io.vertx.up.uca.job.plan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class JobYearly extends AbstractJobAt {
    @Override
    protected LocalDateTime analyze(final LocalDateTime startAt, final LocalTime time, final String segment) {
        final LocalDate startDate = startAt.toLocalDate();
        final LocalDateTime parsed = LocalDateTime.of(startDate, time);
        // Find the day of Year
        LocalDateTime find = parsed.with(TemporalAdjusters.firstDayOfYear());
        find = this.plusWith(find, segment);

        if (find.isBefore(startAt)) {
            find = find.plusYears(1);
        }
        return find;
    }

    @Override
    public String format() {
        return "'Years'=yyyy,'Months'=MM,'Days'=dd,'Time='HH:mm:ss.SSS";
    }
}
