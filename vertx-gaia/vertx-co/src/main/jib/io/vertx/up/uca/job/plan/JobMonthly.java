package io.vertx.up.uca.job.plan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class JobMonthly extends AbstractJobAt {

    /*
     * The time format should be:
     *
     * M,00:15/2,00:16/5 ....
     */

    @Override
    protected LocalDateTime analyze(final LocalDateTime startAt, final LocalTime time, final String segment) {
        final int monthDay = Integer.parseInt(segment);
        // Calculate the New Day
        final LocalDate startDate = startAt.toLocalDate();
        final LocalDateTime parsed = LocalDateTime.of(startDate, time);

        // Find the month next by day
        LocalDateTime find = parsed.with(TemporalAdjusters.firstDayOfMonth());
        find = find.plusDays(monthDay - 1);
        if (find.isBefore(startAt)) {
            find = find.plusMonths(1);
        }
        return find;
    }

    @Override
    public String format() {
        return "'Days'=dd,'Time='HH:mm:ss.SSS";
    }
}
