package io.vertx.up.extension.router.websocket;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.up.extension.AbstractAres;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AresBridge extends AbstractAres {

    public AresBridge(final Vertx vertx) {
        super(vertx);
    }

    @Override
    public void mount(final Router router, final JsonObject config) {

    }
}
