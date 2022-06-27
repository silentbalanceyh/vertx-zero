package io.vertx.up.uca.rs.router;

import io.vertx.core.SockOptions;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.up.uca.rs.Axis;

import java.util.Objects;

public class SockAxis implements Axis<Router> {

    private SockOptions options;
    private Vertx vertxRef;

    public Axis<Router> bind(final Vertx vertxRef, final SockOptions options) {
        this.vertxRef = vertxRef;
        this.options = options;
        return this;
    }

    @Override
    public void mount(final Router router) {
        if (Objects.nonNull(this.options)) {
            final SockJSHandlerOptions optHandler = this.options.configHandler();
            final SockJSBridgeOptions optBridge = this.options.configBridge();
        }
    }
}
