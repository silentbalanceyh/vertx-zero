package io.vertx.rx.micro;

import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;

public class ZeroRxEndurer implements Handler<RoutingContext> {

    public static Handler<RoutingContext> create() {
        return new ZeroRxEndurer();
    }

    private ZeroRxEndurer() {
    }

    @Override
    public void handle(final RoutingContext context) {
        if (context.failed()) {
            final Throwable ex = context.failure();
            ex.printStackTrace();
        }
    }
}
