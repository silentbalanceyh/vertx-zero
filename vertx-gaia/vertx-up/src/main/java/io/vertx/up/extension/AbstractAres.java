package io.vertx.up.extension;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.horizon.uca.log.Annal;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractAres implements Ares {
    private final Vertx vertxRef;
    protected HttpServer server;

    protected HttpServerOptions options;

    public AbstractAres(final Vertx vertxRef) {
        this.vertxRef = vertxRef;
    }

    @Override
    public Ares bind(final HttpServer server, final HttpServerOptions options) {
        this.server = server;
        this.options = options;
        return this;
    }

    public Vertx vertx() {
        return this.vertxRef;
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
