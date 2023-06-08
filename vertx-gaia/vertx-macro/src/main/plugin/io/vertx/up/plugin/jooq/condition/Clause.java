package io.vertx.up.plugin.jooq.condition;

import org.jooq.Condition;
import org.jooq.Field;

import java.util.Objects;

/*
 * Channel for Type
 * 1) Object -> UnifiedClause
 * 2) String -> StringClause
 * 3) Boolean -> BooleanClause
 */
@SuppressWarnings("all")
public interface Clause {

    static Clause get(final Class<?> type) {
        Clause clause = Pool.CLAUSE_MAP.get(type);
        if (Objects.isNull(clause)) {
            clause = Pool.CLAUSE_MAP.get(Object.class);
        }
        return clause;
    }

    Condition where(final Field columnName, final String fieldName, final String op, final Object value);
}
