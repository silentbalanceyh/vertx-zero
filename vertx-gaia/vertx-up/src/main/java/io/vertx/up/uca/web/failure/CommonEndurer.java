package io.vertx.up.uca.web.failure;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * # 「Co」Zero Critical internal failure handler
 *
 * Common handler to handle failure
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CommonEndurer implements Handler<RoutingContext> {

    private CommonEndurer() {
    }

    public static Handler<RoutingContext> create() {
        return new CommonEndurer();
    }

    @Override
    public void handle(final RoutingContext event) {
        if (event.failed()) {
            /*
             * Reply
             */
            final Throwable error = event.failure();
            error.printStackTrace();
        }
    }
}
