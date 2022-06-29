package io.vertx.tp.plugin.stomp.handler;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.stomp.ServerFrame;
import io.vertx.up.atom.secure.Aegis;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface SicFrameHandler extends Handler<ServerFrame> {

    // Build Different Handler as Factory
    static SicFrameHandler connect(final Vertx vertx) {
        return new SicConnectHandler(vertx);
    }

    // Api For Definition
    SicFrameHandler bind(Aegis config);
}
