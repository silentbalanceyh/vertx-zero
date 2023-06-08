package io.vertx.up.backbone.mime.parse;

import io.horizon.exception.WebException;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.backbone.Filler;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class StandardAtomic<T> implements Atomic<T> {

    @Override
    public Epsilon<T> ingest(final RoutingContext context,
                             final Epsilon<T> income)
        throws WebException {
        final Supplier<Filler> fillerFn = Filler.PARAMS.get(income.getAnnotation().annotationType());
        final Filler filler = fillerFn.get();
        final Object value = filler.apply(income.getName(), income.getArgType(), context);
        return null == value ? income.setValue(null) : income.setValue((T) value);
    }
}
