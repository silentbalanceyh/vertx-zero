package io.vertx.up.uca.compare;

import io.vertx.up.util.Ut;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractSameDate extends AbstractSame {

    public AbstractSameDate(final Class<?> type) {
        super(type);
    }

    @Override
    public boolean isAnd(final Object valueOld, final Object valueNew) {
        /*
         * For `Date` type of `Instant`, there provide comparing method
         * for different unit kind fo comparing.
         * 1) Convert to instant first
         * 2) When `unit` is null, do not comparing other kind of here.
         */
        final Instant oldInstant = Ut.parseFull(valueOld.toString())
            .toInstant();
        final Instant newInstant = Ut.parseFull(valueNew.toString())
            .toInstant();
        /*
         * Compared by unit
         */
        final LocalDateTime datetimeOld = Ut.toDateTime(oldInstant);
        final LocalDateTime datetimeNew = Ut.toDateTime(newInstant);
        return this.eqDate(datetimeOld, datetimeNew);
    }

    protected boolean eqDay(final LocalDateTime datetimeOld, final LocalDateTime datetimeNew) {
        final LocalDate dateOld = datetimeOld.toLocalDate();
        final LocalDate dateNew = datetimeNew.toLocalDate();
        return dateOld.isEqual(dateNew);
    }

    protected boolean eqMinute(final LocalDateTime datetimeOld, final LocalDateTime datetimeNew) {
        final LocalTime timeOld = datetimeOld.toLocalTime();
        final LocalTime timeNew = datetimeNew.toLocalTime();
        return this.eqDay(datetimeOld, datetimeNew) &&
            (timeOld.getHour() == timeNew.getHour() && timeOld.getMinute() == timeNew.getMinute());
    }

    public abstract boolean eqDate(final LocalDateTime datetimeOld, final LocalDateTime datetimeNew);
}
