package io.vertx.up.extension.router;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.up.extension.AbstractAres;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AresSock extends AbstractAres {

    AresSock(final Vertx vertx) {
        super(vertx);
    }

    /*
     * WebSocket Extension for user-defined WebSocket server that be connected
     * to current HTTP server by `port`. Here the WebSocket configuration should share
     * HTTP Port instead of standalone. it means that when you configure the WebSocket
     * in your environment, the port number must be the same as HTTP server, if the port
     * does not exist in your HTTP server instances, the WebSocket server will be
     * ignored.
     */
    @Override
    public void mount(final Router router, final JsonObject config) {

    }
}
