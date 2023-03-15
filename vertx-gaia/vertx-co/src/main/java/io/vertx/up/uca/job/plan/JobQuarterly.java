package io.vertx.up.uca.job.plan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class JobQuarterly extends AbstractJobAt {


    @Override
    protected LocalDateTime analyze(final LocalDateTime startAt, final LocalTime time, final String segment) {
        final LocalDate startDate = startAt.toLocalDate();
        final LocalDateTime parsed = LocalDateTime.of(startDate, time);
        // Find the day of Quarter
        LocalDateTime find = this.findQuarter(parsed);
        find = this.plusWith(find, segment);

        if (find.isBefore(startAt)) {
            find = find.plusMonths(3);
        }
        return find;
    }

    protected LocalDateTime findQuarter(final LocalDateTime startAt) {
        final LocalDateTime find = startAt.with(TemporalAdjusters.firstDayOfYear());
        final int month = startAt.getMonthValue();
        if (month <= 3) {
            return find;
        } else if (month <= 6) {
            return find.plusMonths(2);
        } else if (month <= 9) {
            return find.plusMonths(5);
        } else {
            return find.plusMonths(8);
        }
    }

    @Override
    public String format() {
        return "'Months'=MM,'Days'=dd,'Time='HH:mm:ss.SSS";
    }
}
