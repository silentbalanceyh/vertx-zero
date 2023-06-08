package io.vertx.up.backbone.mime.resolver;

import io.horizon.exception.WebException;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.backbone.mime.Resolver;

public class BufferResolver<T> implements Resolver<T> {

    @Override
    @SuppressWarnings("all")
    public Epsilon<T> resolve(final RoutingContext context,
                              final Epsilon<T> income)
        throws WebException {
        final Class<?> clazz = income.getArgType();
        if (Buffer.class == clazz) {
            final Buffer body = context.body().buffer();
            income.setValue((T) body);
        }
        return income;
    }
}
