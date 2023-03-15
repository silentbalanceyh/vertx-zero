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

    // Api For Definition
    FrameWsHandler bind(Aegis config);
}
