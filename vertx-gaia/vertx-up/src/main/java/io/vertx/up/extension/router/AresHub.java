package io.vertx.up.extension.router;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.up.extension.AbstractAres;
import io.vertx.up.extension.Ares;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AresHub extends AbstractAres {
    private final Ares dynamic;

    public AresHub(final Vertx vertxRef) {
        super(vertxRef);
        this.dynamic = new AresDynamic(vertxRef);
    }

    @Override
    public Ares bind(final HttpServer server, final HttpServerOptions options) {
        this.dynamic.bind(server, options);
        return super.bind(server, options);
    }

    @Override
    public void mount(final Router router, final JsonObject config) {
        /*
         * Dynamic Extension for some user-defined router to resolve some spec
         * requirement such as Data Driven System and Origin X etc.
         * Call second method to inject vertx reference.
         */
        this.dynamic.mount(router, config);
    }
}
