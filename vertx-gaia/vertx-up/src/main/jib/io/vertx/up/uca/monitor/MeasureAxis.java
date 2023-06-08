package io.vertx.up.uca.monitor;

import io.horizon.specification.boot.HAxis;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class MeasureAxis implements HAxis<Router> {

    private transient final HAxis<Router> axis;

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
