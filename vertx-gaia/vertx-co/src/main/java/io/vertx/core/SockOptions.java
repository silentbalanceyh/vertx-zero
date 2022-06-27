package io.vertx.core;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SockOptions implements Serializable {
    private final SockJSHandlerOptions optionHandler;
    private final SockJSBridgeOptions optionBridge;

    public SockOptions() {
        this(new JsonObject());
    }

    public SockOptions(final JsonObject input) {
        /*
         * port:
         * config:
         *    handler:
         *    bridge:
         *
         * If the port is the same as `HTTP` server, it will be mount to the
         * same server here Or the Vert.x instance will ignore the WebSocket server
         * here. it means that the WebSocket must be mounted to existing HTTP Server
         * instead.
         */
        final JsonObject handlerJ = Ut.valueJObject(input, KName.HANDLER);
        this.optionHandler = new SockJSHandlerOptions(handlerJ);

        final JsonObject bridgeJ = Ut.valueJObject(input, KName.BRIDGE);
        this.optionBridge = new SockJSBridgeOptions(bridgeJ);
    }

    public SockJSBridgeOptions configBridge() {
        return this.optionBridge;
    }

    public SockJSHandlerOptions configHandler() {
        return this.optionHandler;
    }
}
