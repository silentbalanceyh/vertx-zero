package io.vertx.up.boot.filter;

import io.vertx.core.VertxException;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;

public interface Filter {

    void doFilter(final HttpServerRequest request,
                  final HttpServerResponse response)
        throws IOException, VertxException;

    default void init(final RoutingContext context) {
    }
}
