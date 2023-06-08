package io.vertx.up.plugin.jooq.condition;

import io.vertx.up.util.Ut;
import org.jooq.Condition;
import org.jooq.Field;

@SuppressWarnings("all")
public class ClauseBoolean extends ClauseString {
    @Override
    public Condition where(final Field columnName, final String fieldName, final String op, final Object value) {
        final Class<?> type = value.getClass();
        Object normalized = value;
        if (Ut.isBoolean(value)) {
            normalized = normalized(value, from -> Boolean.valueOf(from.toString()));
        }
        return super.where(columnName, fieldName, op, normalized);
    }
}
