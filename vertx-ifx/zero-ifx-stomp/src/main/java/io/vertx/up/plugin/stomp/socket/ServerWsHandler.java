package io.vertx.up.plugin.stomp.socket;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.stomp.StompServerConnection;
import io.vertx.ext.stomp.StompServerHandler;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public interface ServerWsHandler extends StompServerHandler {

    static ServerWsHandler create(final Vertx vertx) {
        return new ServerStompHandler(vertx);
    }

    /*
     * 「Extension」
     * This method is designed by zero framework for authorization that connect to `zero-rbac` for websocket
     * validation on user data structure, the json part is as following:
     * {
     *     "access_token": "xxxx",
     *     "user": "xxxx",
     *     "habitus": "xxxx",
     *     "session": "xxxx"
     * }
     */
    @Fluent
    StompServerHandler onAuthenticationRequest(StompServerConnection connection, JsonObject principal, Handler<AsyncResult<Boolean>> handler);
}
