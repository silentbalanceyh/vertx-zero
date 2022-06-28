package io.vertx.tp.plugin.stomp.handler;

import io.vertx.core.Vertx;
import io.vertx.ext.stomp.impl.DefaultStompHandler;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BuiltInStompHandler extends DefaultStompHandler {
    public BuiltInStompHandler(final Vertx vertx) {
        super(vertx);
    }
}
