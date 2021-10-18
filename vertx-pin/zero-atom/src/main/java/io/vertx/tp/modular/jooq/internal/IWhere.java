package io.vertx.tp.modular.jooq.internal;

import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.error._417ConditionWhereException;
import io.vertx.up.fn.Fn;
import org.jooq.Condition;
import org.jooq.Field;

import java.util.Iterator;
import java.util.List;

class IWhere {
    static Condition unique(final DataMatrix matrix) {
        final Iterator<String> it = matrix.getAttributes().iterator();
        Condition condition = IWhere.Cond.eq(it.next(), matrix);
        while (it.hasNext()) {
            condition = condition.and(IWhere.Cond.eq(it.next(), matrix));
        }
        return condition;
    }

    static Condition key(final DataMatrix matrix) {
        final Iterator<String> it = matrix.getKeys().iterator();
        Fn.outWeb(!it.hasNext(), _417ConditionWhereException.class, IWhere.class);
        Condition condition = IWhere.Cond.eq(it.next(), matrix);
        while (it.hasNext()) {
            condition = condition.and(IWhere.Cond.eq(it.next(), matrix));
        }
        return condition;
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
