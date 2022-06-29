package io.vertx.tp.plugin.stomp.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.stomp.StompServerConnection;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.impl.DefaultStompHandler;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SicStompServerHandler extends DefaultStompHandler implements SicServerHandler {

    public SicStompServerHandler(final Vertx vertx) {
        super(vertx);
    }

    @Override
    public StompServerHandler onAuthenticationRequest(final StompServerConnection connection,
                                                      final JsonObject principal,
                                                      final Handler<AsyncResult<Boolean>> handler) {
        System.out.println(principal);
        return null;
    }
}
