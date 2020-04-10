package io.vertx.tp.modular.jooq.internal;

import io.vertx.tp.atom.modeling.element.DataMatrix;
import org.jooq.Condition;
import org.jooq.Field;

@SuppressWarnings("unchecked")
class Cond {
    /* = 操作 */
    static Condition eq(final String field,
                        final DataMatrix matrix) {
        final Field column = Meta.field(field, matrix);
        final Object value = matrix.getValue(field);
        return column.eq(value);
    }
}
