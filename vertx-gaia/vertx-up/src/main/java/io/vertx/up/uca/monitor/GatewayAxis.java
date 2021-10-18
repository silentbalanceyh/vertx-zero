package io.vertx.up.uca.monitor;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.up.uca.rs.Axis;

public class GatewayAxis implements Axis<Router> {

    private transient final Vertx vertx;

    public GatewayAxis(final Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void mount(final Router router) {

    }
}
