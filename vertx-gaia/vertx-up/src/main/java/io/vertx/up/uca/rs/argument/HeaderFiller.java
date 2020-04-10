package io.vertx.up.uca.rs.argument;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.uca.rs.Filler;
import io.vertx.up.runtime.ZeroSerializer;

public class HeaderFiller implements Filler {
    @Override
    public Object apply(final String name,
                        final Class<?> paramType,
                        final RoutingContext context) {
        // Extract request from header
        final HttpServerRequest request = context.request();
        return ZeroSerializer.getValue(paramType, request.getHeader(name));
    }
}
