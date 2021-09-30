package io.vertx.up.uca.monitor;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.web.Router;
import io.vertx.up.eon.ID;
import io.vertx.up.eon.Orders;
import io.vertx.up.uca.monitor.meansure.Quota;
import io.vertx.up.uca.rs.Axis;

import javax.ws.rs.core.MediaType;
import java.util.concurrent.ConcurrentMap;

public class ServiceAxis implements Axis<Router> {

    private transient final Vertx vertx;
    private transient final HealthChecks healthChecks;

    public ServiceAxis(final Vertx vertx) {
        this.vertx = vertx;
        this.healthChecks = HealthChecks.create(vertx);
        /* First for UxPool */
        final ConcurrentMap<String, Quota> registryMap = Quota.getRegistry(vertx);
        registryMap.forEach(this.healthChecks::register);
    }

    @Override
    public void mount(final Router router) {
        final HealthCheckHandler handler = HealthCheckHandler
            .createWithHealthChecks(this.healthChecks);
        /*
         * Monitor Address
         */
        router.get(ID.Addr.MONITOR_PATH).order(Orders.MONITOR)
            .produces(MediaType.APPLICATION_JSON)
            .handler(handler);
    }
}
