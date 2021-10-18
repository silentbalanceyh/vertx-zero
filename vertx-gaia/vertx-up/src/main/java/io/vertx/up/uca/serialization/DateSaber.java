package io.vertx.up.uca.serialization;

import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Calendar;
import java.util.Date;

/**
 * Date, Calendar
 */
public class DateSaber extends BaseSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.getNull(() ->
                Fn.getSemi(Date.class == paramType ||
                        Calendar.class == paramType, getLogger(),
                    () -> {
                        verifyInput(!Ut.isDate(literal), paramType, literal);
                        final Date reference = Ut.parse(literal);
                        if (Calendar.class == paramType) {
                            // Specific date format
                            final Calendar calendar = Calendar.getInstance();
                            calendar.setTime(reference);
                            return calendar;
                        }
                        return reference;
                    }, Date::new),
            paramType, literal);
    }

    @Override
    public <T> Object from(final T input) {
        return Fn.getNull(() -> {
            Object reference = null;
            if (input instanceof Date) {
                final Date date = (Date) input;
                reference = date.getTime();
            } else if (input instanceof Calendar) {
                final Calendar date = (Calendar) input;
                reference = date.getTime().getTime();
            }
            return reference;
        }, input);
    }
}
