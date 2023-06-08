package io.vertx.up.plugin.jooq.condition;

import org.jooq.Condition;
import org.jooq.Field;

/*
 * Expression analyzing for
 * 1) Field -> Antlr
 * 2) Antlr -> Condition ( by op & value )
 * 3) Result should be condition
 */
@SuppressWarnings("all")
public interface Term {

    Condition where(final Field field, final String fieldName, final Object value);
}
