package io.vertx.tp.plugin.jooq.condition.date;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.time.LocalDate;

@SuppressWarnings("all")
public class TermDGt extends AbstractDTerm {
    @Override
    public Condition where(final Field field, final String fieldName, final Object value) {
        return this.toDate(field, () -> {
            final LocalDate date = this.toDate(value);
            return field.gt(date.atStartOfDay());
        }, () -> DSL.field(fieldName).gt(value));
    }
}
