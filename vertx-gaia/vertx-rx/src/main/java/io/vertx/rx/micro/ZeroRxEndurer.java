package io.vertx.rx.micro;

import io.vertx.core.Handler;
import io.vertx.rxjava3.ext.web.RoutingContext;

public class ZeroRxEndurer implements Handler<RoutingContext> {

    private ZeroRxEndurer() {
    }

    public static Handler<RoutingContext> create() {
        return new ZeroRxEndurer();
    }

    @Override
    public void handle(final RoutingContext context) {
        if (context.failed()) {
            final Throwable ex = context.failure();
            ex.printStackTrace();
        }
    }
}
