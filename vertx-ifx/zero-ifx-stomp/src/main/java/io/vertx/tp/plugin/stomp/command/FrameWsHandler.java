package io.vertx.tp.plugin.stomp.command;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.stomp.ServerFrame;
import io.vertx.up.atom.secure.Aegis;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface FrameWsHandler extends Handler<ServerFrame> {

    // Build Different Handler as Factory
    static FrameWsHandler connector(final Vertx vertx) {
        return new FrameConnector(vertx);
    }

    static FrameWsHandler receiver(final Vertx vertx) {
        return new FrameReceiver(vertx);
    }

    static FrameWsHandler writer(final Vertx vertx) {
        return new FrameWriter(vertx);
    }

    // Api For Definition
    FrameWsHandler bind(Aegis config);
}
