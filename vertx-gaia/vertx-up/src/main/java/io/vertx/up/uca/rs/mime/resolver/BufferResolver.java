package io.vertx.up.uca.rs.mime.resolver;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.exception.WebException;
import io.vertx.up.uca.rs.mime.Resolver;

public class BufferResolver<T> implements Resolver<T> {

    @Override
    public Epsilon<T> resolve(final RoutingContext context,
                              final Epsilon<T> income)
            throws WebException {
        // Buffer Resolver
        return income;
    }
}
