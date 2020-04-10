package io.vertx.up.uca.rs.argument;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.uca.rs.Filler;
import io.vertx.up.runtime.ZeroSerializer;

public class PathFiller implements Filler {
    @Override
    public Object apply(final String name,
                        final Class<?> paramType,
                        final RoutingContext context) {
        // 1. Get path information
        return ZeroSerializer.getValue(paramType, context.pathParam(name));
    }
}
