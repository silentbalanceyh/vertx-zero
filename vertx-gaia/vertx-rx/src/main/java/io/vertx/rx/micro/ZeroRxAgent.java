package io.vertx.rx.micro;

import io.reactivex.Single;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Route;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.rx.rs.router.EventAxis;
import io.vertx.rx.rs.router.RouterAxis;
import io.vertx.up.annotations.Agent;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.ServerType;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.rs.Axis;
import io.vertx.up.util.Ut;
import io.vertx.up.verticle.ZeroAtomic;

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

    private static final Annal LOGGER = Annal.get(ZeroRxAgent.class);
    private transient final String NAME = getClass().getSimpleName();

    @Override
    public void start() {
        /** 1.Call router hub to mount commont **/
        final Axis<Router> routerAxiser = Fn.poolThread(Pool.ROUTERS,
                () -> Ut.instance(RouterAxis.class));
        /** 2.Call route hub to mount defined **/
        final Axis<Router> axiser = Fn.poolThread(Pool.EVENTS,
                () -> Ut.instance(EventAxis.class));

        /** 3.Get the default HttpServer Options **/
        ZeroAtomic.RX_OPTS.forEach((port, option) -> {
            /** 3.1.Single server processing **/
            final HttpServer server = vertx.createHttpServer(option);
            /** 3.2. Build router with current option **/
            final Router router = Router.router(vertx);

            routerAxiser.mount(router);
            axiser.mount(router);

            /** 3.3. Listen for router on the server **/
            final Single<HttpServer> result =
                    server.requestHandler(router).rxListen();
            /** 3.4. Log output **/
            {
                result.subscribe((rxServer) -> {
                    recordServer(option, router);
                });
            }
        });
    }

    private void recordServer(final HttpServerOptions options,
                              final Router router) {
        final Integer port = options.getPort();
        final AtomicInteger out = ZeroAtomic.RX_START_LOGS.get(port);
        if (Values.ZERO == out.getAndIncrement()) {
            // 1. Build logs for current server;
            final String portLiteral = String.valueOf(port);
            LOGGER.info(Info.RX_SERVERS, NAME, deploymentID(),
                    portLiteral);
            final List<Route> routes = router.getRoutes();
            final Map<String, Route> routeMap = new TreeMap<>();
            for (final Route route : routes) {
                // 2.Route
                final String path = null == route.getPath() ? "/*" : route.getPath();
                routeMap.put(path, route);
            }
            routeMap.forEach((path, route) ->
                    LOGGER.info(Info.MAPPED_ROUTE, NAME, path,
                            route.toString()));
            // 3. Endpoint Publish
            final String address =
                    MessageFormat.format("http://{0}:{1}/",
                            options.getHost(), portLiteral);
            LOGGER.info(Info.RX_LISTEN, NAME, address);
        }
    }
}
