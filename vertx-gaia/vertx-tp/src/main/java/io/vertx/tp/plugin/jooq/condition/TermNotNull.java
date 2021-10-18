package io.vertx.tp.plugin.jooq.condition;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

@SuppressWarnings("all")
public class TermNotNull implements Term {
    @Override
    public Condition where(final Field field, final String fieldName, final Object value) {
        return DSL.field(fieldName).isNotNull();
    }
}
