package io.vertx.up.uca.monitor;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.up.uca.rs.Axis;

public class MeasureAxis implements Axis<Router> {

    private transient final Axis<Router> axis;

    public MeasureAxis(final Vertx vertx, final boolean isGateway) {
        if (isGateway) {
            this.axis = new GatewayAxis(vertx);
        } else {
            this.axis = new ServiceAxis(vertx);
        }
    }

    @Override
    public void mount(final Router router) {
        this.axis.mount(router);
    }
}
