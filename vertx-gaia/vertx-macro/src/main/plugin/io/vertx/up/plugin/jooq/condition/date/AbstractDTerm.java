package io.vertx.up.plugin.jooq.condition.date;

import io.vertx.up.plugin.jooq.condition.Term;
import io.vertx.up.util.Ut;
import org.jooq.Condition;
import org.jooq.Field;

import java.time.LocalDate;
import java.util.Date;
import java.util.function.Supplier;

public abstract class AbstractDTerm implements Term {

    protected LocalDate toDate(final Object value) {
        final Date instance = (Date) value;
        return Ut.toDate(instance.toInstant());
    }

    @SuppressWarnings("all")
    protected Condition toDate(final Field field,
                               final Supplier<Condition> dateSupplier, final Supplier<Condition> otherSupplier) {
        final Class<?> type = field.getType();
        if (LocalDate.class == type) {
            return dateSupplier.get();
        } else {
            return otherSupplier.get();
        }
    }
}
