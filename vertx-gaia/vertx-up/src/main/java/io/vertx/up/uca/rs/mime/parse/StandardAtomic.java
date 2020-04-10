package io.vertx.up.uca.rs.mime.parse;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.exception.WebException;
import io.vertx.up.uca.rs.Filler;

@SuppressWarnings("unchecked")
public class StandardAtomic<T> implements Atomic<T> {

    @Override
    public Epsilon<T> ingest(final RoutingContext context,
                             final Epsilon<T> income)
            throws WebException {
        final Filler filler = Filler.PARAMS.get(income.getAnnotation().annotationType());
        final Object value = filler.apply(income.getName(), income.getArgType(), context);
        return null == value ? income.setValue(null) : income.setValue((T) value);
    }
}
