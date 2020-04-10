package io.vertx.tp.modular.jooq.internal;

import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.error._417ConditionWhereException;
import io.vertx.up.fn.Fn;
import org.jooq.Condition;

import java.util.Iterator;
import java.util.List;

class Where {

    static Condition unique(final DataMatrix matrix) {
        final Iterator<String> it = matrix.getAttributes().iterator();
        Condition condition = Cond.eq(it.next(), matrix);
        while (it.hasNext()) {
            condition = condition.and(Cond.eq(it.next(), matrix));
        }
        return condition;
    }

    static Condition key(final DataMatrix matrix) {
        final Iterator<String> it = matrix.getKeys().iterator();
        Fn.outWeb(!it.hasNext(), _417ConditionWhereException.class, Where.class);
        Condition condition = Cond.eq(it.next(), matrix);
        while (it.hasNext()) {
            condition = condition.and(Cond.eq(it.next(), matrix));
        }
        return condition;
    }

    static Condition keys(final List<DataMatrix> matrixList) {
        final Iterator<DataMatrix> it = matrixList.iterator();
        Fn.outWeb(!it.hasNext(), _417ConditionWhereException.class, Where.class);
        Condition condition = key(it.next());
        while (it.hasNext()) {
            condition = condition.or(key(it.next()));
        }
        return condition;
    }
}
