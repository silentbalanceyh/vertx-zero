package io.vertx.tp.plugin.stomp.command;

import io.vertx.core.Vertx;
import io.vertx.ext.stomp.ServerFrame;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class FrameReceiver extends AbstractFrameHandler {
    FrameReceiver(final Vertx vertx) {
        super(vertx);
    }

    @Override
    public void handle(final ServerFrame event) {
        System.out.println(event);
    }
}
