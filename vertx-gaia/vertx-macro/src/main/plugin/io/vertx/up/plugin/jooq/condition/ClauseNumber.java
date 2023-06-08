package io.vertx.up.plugin.jooq.condition;

import io.vertx.up.util.Ut;
import org.jooq.Condition;
import org.jooq.Field;

@SuppressWarnings("all")
public class ClauseNumber extends ClauseString {
    @Override
    public Condition where(final Field columnName, final String fieldName, final String op, final Object value) {
        final Class<?> type = value.getClass();
        Object normalized = value;
        if (Ut.isInteger(value)) {
            normalized = normalized(value, from -> {
                if (Long.class == type || long.class == type) {
                    return Long.valueOf(from.toString());
                } else if (Short.class == type || short.class == type) {
                    return Short.valueOf(from.toString());
                } else {
                    return Integer.valueOf(from.toString());
                }
            });
        }
        return super.where(columnName, fieldName, op, value);
    }
}
