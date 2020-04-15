package io.vertx.up.uca.condition.date;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.time.LocalDate;

@SuppressWarnings("all")
public class TermDEq extends AbstractDTerm {
    @Override
    public Condition where(final Field field, final String fieldName, final Object value) {
        return this.toDate(field, () -> {
            final LocalDate date = this.toDate(value);
            return field.between(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        }, () -> DSL.field(fieldName).eq(value));
    }
}
