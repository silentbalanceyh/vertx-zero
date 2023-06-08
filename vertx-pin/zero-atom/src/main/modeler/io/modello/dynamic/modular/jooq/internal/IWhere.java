package io.modello.dynamic.modular.jooq.internal;

import io.vertx.mod.atom.error._417ConditionWhereException;
import io.vertx.mod.atom.modeling.element.DataMatrix;
import io.vertx.up.fn.Fn;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class IWhere {

    static Condition key(final DataMatrix matrix) {
        final Set<String> keys = matrix.getKeys();
        Fn.outWeb(keys.isEmpty(), _417ConditionWhereException.class, IWhere.class);
        final Set<Condition> conditions = keys.stream()
            .map(field -> IWhere.Cond.eq(field, matrix))
            .collect(Collectors.toSet());
        return DSL.and(conditions);
    }

    static Condition keys(final List<DataMatrix> matrixList) {
        final Iterator<DataMatrix> it = matrixList.iterator();
        Fn.outWeb(!it.hasNext(), _417ConditionWhereException.class, IWhere.class);
        Condition condition = key(it.next());
        while (it.hasNext()) {
            condition = condition.or(key(it.next()));
        }
        return condition;
    }

    /* = 操作 */
    public interface Cond {
        @SuppressWarnings("all")
        public static Condition eq(final String field,
                                   final DataMatrix matrix) {
            final Field column = Meta.field(field, matrix);
            final Object value = matrix.getValue(field);
            return column.eq(value);
        }
    }
}
