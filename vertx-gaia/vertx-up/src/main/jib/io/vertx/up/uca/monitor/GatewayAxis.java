package io.vertx.up.uca.monitor;

import io.horizon.specification.boot.HAxis;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class GatewayAxis implements HAxis<Router> {

    private transient final Vertx vertx;

    public GatewayAxis(final Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void mount(final Router router) {

    }
}
