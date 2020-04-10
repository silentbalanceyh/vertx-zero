package io.vertx.up.uca.rs.router.monitor;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.web.Router;
import io.vertx.up.eon.ID;
import io.vertx.up.eon.Orders;
import io.vertx.up.uca.rs.Axis;

import javax.ws.rs.core.MediaType;

public class ServiceAxis implements Axis<Router> {

    private transient final Vertx vertx;
    private transient final HealthChecks healthChecks;

    public ServiceAxis(final Vertx vertx) {
        this.vertx = vertx;
        healthChecks = HealthChecks.create(vertx);
        /* First for UxPool */
        healthChecks.register("pool/habitus", Quota.pool(vertx, "vertx-web.sessions.habitus"));
    }

    @Override
    public void mount(final Router router) {
        final HealthCheckHandler handler = HealthCheckHandler
                .createWithHealthChecks(healthChecks);
        /*
         * Monitor Address
         */
        router.get(ID.Addr.MONITOR_PATH).order(Orders.MONITOR)
                .produces(MediaType.APPLICATION_JSON)
                .handler(handler);
    }
}
