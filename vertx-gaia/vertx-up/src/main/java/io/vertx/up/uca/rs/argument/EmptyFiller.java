package io.vertx.up.uca.rs.argument;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.uca.rs.Filler;

public class EmptyFiller implements Filler {
    @Override
    public Object apply(final String name,
                        final Class<?> paramType,
                        final RoutingContext context) {
        return null;
    }
}
