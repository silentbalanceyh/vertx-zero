package io.vertx.rx.micro;

import io.horizon.eon.VValue;
import io.horizon.specification.boot.HAxis;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.rx.rs.router.EventAxis;
import io.vertx.rx.rs.router.RouterAxis;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.http.HttpServer;
import io.vertx.rxjava3.ext.web.Route;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.up.annotations.Agent;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.runtime.ZeroOption;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Rx Agent
 */
@Agent(type = ServerType.RX)
public class ZeroRxAgent extends AbstractVerticle {

    private static final Cc<String, HAxis<Router>> CC_ROUTES = Cc.openThread();
    private static final Cc<String, HAxis<Router>> CC_EVENTS = Cc.openThread();
    private static final Annal LOGGER = Annal.get(ZeroRxAgent.class);
    private transient final String NAME = this.getClass().getSimpleName();

    @Override
    public void start() {
        /* 1.Call router hub to mount commont **/
        final HAxis<Router> routerAxiser = CC_ROUTES.pick(() -> Ut.instance(RouterAxis.class));
        // Fn.po?lThread(Pool.ROUTERS, () -> Ut.instance(RouterAxis.class));
        /* 2.Call route hub to mount defined **/
        final HAxis<Router> axiser = CC_EVENTS.pick(() -> Ut.instance(EventAxis.class));
        // Fn.po?lThread(Pool.EVENTS, () -> Ut.instance(EventAxis.class));

        /* 3.Get the default HttpServer Options **/
        ZeroOption.getRxOptions().forEach((port, option) -> {
            /* 3.1.Single server processing **/
            final HttpServer server = this.vertx.createHttpServer(option);
            /* 3.2. Build router with current option **/
            final Router router = Router.router(this.vertx);

            routerAxiser.mount(router);
            axiser.mount(router);

            /* 3.3. Listen for router on the server **/
            final Single<HttpServer> result =
                server.requestHandler(router).rxListen();
            /* 3.4. Log output **/
            {
                result.subscribe((rxServer) -> this.recordServer(option, router)).dispose();
            }
        });
    }

    private void recordServer(final HttpServerOptions options,
                              final Router router) {
        final Integer port = options.getPort();
        final AtomicInteger out = ZeroOption.ATOMIC_LOG.get(port);
        if (VValue.ZERO == out.getAndIncrement()) {
            // 1. Build logs for current server;
            final String portLiteral = String.valueOf(port);
            LOGGER.info(Info.RX_SERVERS, this.NAME, this.deploymentID(),
                portLiteral);
            final List<Route> routes = router.getRoutes();
            final Map<String, Route> routeMap = new TreeMap<>();
            for (final Route route : routes) {
                // 2.Route
                final String path = null == route.getPath() ? "/*" : route.getPath();
                routeMap.put(path, route);
            }
            routeMap.forEach((path, route) ->
                LOGGER.info(Info.MAPPED_ROUTE, this.NAME, path,
                    route.toString()));
            // 3. Endpoint Publish
            final String address =
                MessageFormat.format("http://{0}:{1}/",
                    options.getHost(), portLiteral);
            LOGGER.info(Info.RX_LISTEN, this.NAME, address);
        }
    }
}
