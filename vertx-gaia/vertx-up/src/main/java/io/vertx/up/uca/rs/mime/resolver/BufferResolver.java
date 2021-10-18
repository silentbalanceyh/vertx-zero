package io.vertx.up.uca.rs.mime.resolver;

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.exception.WebException;
import io.vertx.up.uca.rs.mime.Resolver;

public class BufferResolver<T> implements Resolver<T> {

    @Override
    @SuppressWarnings("all")
    public Epsilon<T> resolve(final RoutingContext context,
                              final Epsilon<T> income)
        throws WebException {
        final Class<?> clazz = income.getArgType();
        if (Buffer.class == clazz) {
            final Buffer body = context.getBody();
            income.setValue((T) body);
        }
        return income;
    }
}
