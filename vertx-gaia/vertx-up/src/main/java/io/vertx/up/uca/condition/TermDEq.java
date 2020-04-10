package io.vertx.up.uca.condition;

import io.vertx.up.util.Ut;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.time.LocalDate;
import java.util.Date;

@SuppressWarnings("all")
public class TermDEq implements Term {
    @Override
    public Condition where(final Field field, final String fieldName, final Object value) {
        final Class<?> type = field.getType();
        if (LocalDate.class == type) {
            final Date instant = (Date) value;
            final LocalDate date = Ut.toDate(instant.toInstant());
            return field.between(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        } else {
            return DSL.field(fieldName).eq(value);
        }
    }
}
