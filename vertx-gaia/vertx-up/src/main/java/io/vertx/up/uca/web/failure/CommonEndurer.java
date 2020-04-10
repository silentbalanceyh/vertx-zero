package io.vertx.up.uca.web.failure;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * Common handler to handle failure
 */
public class CommonEndurer implements Handler<RoutingContext> {

    public static Handler<RoutingContext> create() {
        return new CommonEndurer();
    }

    private CommonEndurer() {
    }

    @Override
    public void handle(final RoutingContext event) {
        if (event.failed()) {
            event.failure().printStackTrace();
        }
    }
}
