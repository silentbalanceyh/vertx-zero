package io.vertx.up.uca.job.plan;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class JobWeekly extends AbstractJobAt {

    /*
     * The time format should be:
     *
     * W,00:15/2,00:16/4 ....
     */

    @Override
    protected LocalDateTime analyze(final LocalDateTime startAt, final LocalTime time, final String segment) {
        final int weekDay = Integer.parseInt(segment);
        // Calculate the New Day
        final LocalDate startDate = startAt.toLocalDate();
        final LocalDateTime parsed = LocalDateTime.of(startDate, time);

        // Find current week information
        LocalDateTime weekFind = this.findWeek(parsed, 0, weekDay);
        if (weekFind.isBefore(startAt)) {
            weekFind = this.findWeek(parsed, 1, weekDay);
        }
        return weekFind;
    }

    protected LocalDateTime findWeek(final LocalDateTime date, final int week, final int dayOfWeek) {
        return date.plusWeeks(week - 1).with(TemporalAdjusters.next(DayOfWeek.of(dayOfWeek)));
    }

    @Override
    public String format() {
        return "'Days'=dd,'Time='HH:mm:ss.SSS";
    }
}
