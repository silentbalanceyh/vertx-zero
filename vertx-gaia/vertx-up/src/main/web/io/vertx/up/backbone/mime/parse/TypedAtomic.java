package io.vertx.up.backbone.mime.parse;

import io.horizon.exception.WebException;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.unity.Ux;

@SuppressWarnings("unchecked")
public class TypedAtomic<T> implements Atomic<T> {
    @Override
    public Epsilon<T> ingest(final RoutingContext context,
                             final Epsilon<T> income)
        throws WebException {
        final Class<?> paramType = income.getArgType();
        // Old:  TypedArgument.analyzeAgent
        final Object returnValue = Ux.toParameter(context, paramType);
        return null == returnValue ? income.setValue(null) : income.setValue((T) returnValue);
    }
}
