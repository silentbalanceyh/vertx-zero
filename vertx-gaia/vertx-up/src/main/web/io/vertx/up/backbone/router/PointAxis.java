package io.vertx.up.backbone.router;

import io.horizon.specification.boot.HAxis;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.up.eon.KWeb;
import io.vertx.up.uca.registry.Uddi;
import io.vertx.up.uca.registry.UddiJet;

/**
 * Router mounter for api gateway.
 */
public class PointAxis implements HAxis<Router> {

    private transient final HttpServerOptions options;
    private transient final Vertx vertx;

    public PointAxis(final HttpServerOptions options,
                     final Vertx vertx) {
        this.options = options;
        this.vertx = vertx;
    }

    @Override
    public void mount(final Router router) {
        /* Breaker and Dispatch **/
        final UddiJet jet = Uddi.discovery(this.getClass());
        router.route("/*").order(KWeb.ORDER.EVENT).handler(
            jet.bind(this.vertx).bind(this.options).handler()
        );
    }
}
