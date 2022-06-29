package io.vertx.up.extension;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroAnno;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractAres implements Ares {


    private static final Set<Remind> SOCKS = ZeroAnno.getSocks();
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

    protected Set<Remind> socks(final Boolean secure) {
        if (Objects.isNull(secure)) {
            return SOCKS;
        }
        if (secure) {
            // Secure
            return SOCKS.stream()
                .filter(Remind::isSecure)
                .collect(Collectors.toSet());
        } else {
            // Publish
            return SOCKS.stream()
                .filter(sock -> !sock.isSecure())
                .collect(Collectors.toSet());
        }
    }
}
