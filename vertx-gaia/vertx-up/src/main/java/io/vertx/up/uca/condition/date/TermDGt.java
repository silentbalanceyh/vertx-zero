package io.vertx.up.uca.condition.date;

import io.vertx.up.uca.condition.Term;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

@SuppressWarnings("all")
public class TermDGt implements Term {
    @Override
    public Condition where(final Field field, final String fieldName, final Object value) {
        return DSL.field(fieldName).gt(value);
    }
}
