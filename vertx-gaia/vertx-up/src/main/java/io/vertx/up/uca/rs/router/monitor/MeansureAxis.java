package io.vertx.up.uca.rs.router.monitor;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.up.uca.rs.Axis;

public class MeansureAxis implements Axis<Router> {

    private transient final Axis<Router> axiser;

    public MeansureAxis(final Vertx vertx, final boolean isGateway) {
        if (isGateway) {
            axiser = new GatewayAxis(vertx);
        } else {
            axiser = new ServiceAxis(vertx);
        }
    }

    @Override
    public void mount(final Router router) {
        axiser.mount(router);
    }
}
