package io.vertx.up.uca.serialization;

import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class Java8DataTimeSaber extends BaseSaber {
    @Override
    public <T> Object from(final T input) {
        return Fn.runOr(() -> {
            Object reference = null;
            if (input instanceof LocalDate) {
                final LocalDate date = (LocalDate) input;
                reference = date.toString();
            } else if (input instanceof LocalDateTime) {
                final LocalDateTime dateTime = (LocalDateTime) input;
                reference = dateTime.toString();
            } else if (input instanceof LocalTime) {
                final LocalTime time = (LocalTime) input;
                reference = time.toString();
            }
            return reference;
        }, input);
    }

    @Override
    public Object from(final Class<?> paramType, final String literal) {
        return Fn.runOr(() ->
                Fn.runOr(Date.class == paramType ||
                        Calendar.class == paramType, this.getLogger(),
                    () -> {
                        this.verifyInput(!Ut.isDate(literal), paramType, literal);
                        final Date reference = Ut.parse(literal);
                        if (LocalTime.class == paramType) {
                            return Ut.toTime(literal);
                        } else if (LocalDate.class == paramType) {
                            return Ut.toDate(literal);
                        } else if (LocalDateTime.class == paramType) {
                            return Ut.toDateTime(literal);
                        }
                        return reference;
                    }, Date::new),
            paramType, literal);
    }
}
