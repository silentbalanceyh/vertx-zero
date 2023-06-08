package io.vertx.up.plugin.jooq.condition;

import io.vertx.up.util.Ut;
import org.jooq.Condition;
import org.jooq.Field;

import java.util.Objects;

@SuppressWarnings("all")
public class ClauseInstant extends ClauseString {
    @Override
    public Condition where(final Field columnName, final String fieldName, final String op, final Object value) {
        /*
         * value parsing for instant
         */
        if (Objects.isNull(value)) {
            // TODO: Fix fast issue for null
            return null;
        } else {
            final Class<?> type = value.getClass();
            final Object normalized = Ut.parseFull(value.toString());
            final Term term = this.termDate(op);
            return term.where(columnName, fieldName, normalized);
        }
    }
}
