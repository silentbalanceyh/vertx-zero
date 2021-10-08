package io.vertx.up.uca.monitor;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.web.Router;
import io.vertx.up.eon.Orders;
import io.vertx.up.uca.monitor.meansure.Quota;
import io.vertx.up.uca.monitor.meansure.QuotaConnect;
import io.vertx.up.uca.rs.Axis;

import javax.ws.rs.core.MediaType;
import java.util.concurrent.ConcurrentMap;

public class ServiceAxis implements Axis<Router> {

    private transient final Vertx vertx;

    public ServiceAxis(final Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void mount(final Router router) {

        final HealthChecks health = HealthChecks.create(this.vertx);
        /* First for UxPool */
        final ConcurrentMap<String, Quota> registryMap = Quota.getRegistry(this.vertx);
        registryMap.forEach(health::register);

        final HealthCheckHandler handler = HealthCheckHandler
            .createWithHealthChecks(health);
        /*
         * Monitor Address
         */
        router.get(QuotaConnect.routePath()).order(Orders.MONITOR)
            .produces(MediaType.APPLICATION_JSON)
            .handler(handler);
    }
}
