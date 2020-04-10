package io.vertx.up.uca.rs.mime.parse;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.exception.WebException;
import io.vertx.up.uca.serialization.TypedArgument;

@SuppressWarnings("unchecked")
public class TypedAtomic<T> implements Atomic<T> {
    @Override
    public Epsilon<T> ingest(final RoutingContext context,
                             final Epsilon<T> income)
            throws WebException {
        final Class<?> paramType = income.getArgType();
        final Object returnValue = TypedArgument.analyze(context, paramType);
        return null == returnValue ? income.setValue(null) : income.setValue((T) returnValue);
    }
}
