package io.vertx.up.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.up.annotations.Agent;
import io.vertx.up.eon.ID;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.Etat;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroAeon;
import io.vertx.up.runtime.ZeroGrid;
import io.vertx.up.runtime.ZeroHeart;
import io.vertx.up.uca.registry.Uddi;
import io.vertx.up.uca.registry.UddiRegistry;
import io.vertx.up.uca.rs.Axis;
import io.vertx.up.uca.rs.router.*;
import io.vertx.up.uca.rs.router.monitor.MeansureAxis;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Default Http Server agent for router handlers.
 * Once you have defined another Agent, the default agent will be replaced.
 * Recommend: Do not modify any agent that vertx zero provided.
 */
@Agent
public class ZeroHttpAgent extends AbstractVerticle {

    private static final Annal LOGGER = Annal.get(ZeroHttpAgent.class);

    private static final ConcurrentMap<Integer, String> SERVICES =
            ZeroGrid.getServerNames();

    @Override
    public void start() {
        /* 1.Call router hub to mount commont **/
        final Axis<Router> routerAxiser = Fn.poolThread(Pool.ROUTERS,
                () -> new RouterAxis(this.vertx));

        /* 2.Call route hub to mount defined **/
        final Axis<Router> axiser = Fn.poolThread(Pool.EVENTS,
                EventAxis::new);
        final Axis<Router> dynamic = Fn.poolThread(Pool.DYNAMICS,
                DynamicAxis::new);

        /* 3.Call route hub to mount walls **/
        final Axis<Router> wallAxiser = Fn.poolThread(Pool.WALLS,
                () -> Ut.instance(WallAxis.class, this.vertx));

        /* 4.Call route hub to mount filters **/
        final Axis<Router> filterAxiser = Fn.poolThread(Pool.FILTERS,
                FilterAxis::new);

        /* 5.Call route to mount meansure **/
        final Axis<Router> monitorAxiser = Fn.poolThread(Pool.MEANSURES,
                () -> new MeansureAxis(this.vertx, false));
        /* Get the default HttpServer Options **/
        ZeroAtomic.HTTP_OPTS.forEach((port, option) -> {
            /* Single server processing **/
            final HttpServer server = this.vertx.createHttpServer(option);

            /* Build router with current option **/
            final Router router = Router.router(this.vertx);

            /*
             * It's new engine for routing dynamic deployment in zero framework
             * The class `ZeroAeon` could manage routing reference that stored into
             * zero framework `Router` pool here
             * **/
            ZeroAeon.initialize(router);

            // Router
            routerAxiser.mount(router);
            // Wall
            wallAxiser.mount(router);
            // Event
            axiser.mount(router);
            // Meansure
            monitorAxiser.mount(router);
            {
                /*
                 * Dynamic Extension for some user-defined router to resolve some spec
                 * requirement such as Data Driven System and Origin X etc.
                 * Call second method to inject vertx reference.
                 */
                // This step is required for bind vertx instance
                ((DynamicAxis) dynamic).bind(this.vertx).mount(router);
            }
            // Filter
            filterAxiser.mount(router);
            /* Listen for router on the server **/
            server.requestHandler(router).listen();
            {
                // Log output
                this.registryServer(option, router);
            }
        });
    }

    @Override
    public void stop() {
        Ut.itMap(ZeroAtomic.HTTP_OPTS, (port, config) -> {
            // Enabled micro mode.
            if (ZeroHeart.isEtcd()) {
                // Template call registry to modify the status of current service.
                final UddiRegistry registry = Uddi.registry(this.getClass());
                registry.registryHttp(ZeroHttpAgent.SERVICES.get(port), config, Etat.STOPPED);
            }
        });
    }

    private void registryServer(final HttpServerOptions options,
                                final Router router) {
        final Integer port = options.getPort();
        final AtomicInteger out = ZeroAtomic.HTTP_START_LOGS.get(port);
        if (Values.ZERO == out.getAndIncrement()) {
            // 1. Build logs for current server;
            final String portLiteral = String.valueOf(port);
            ZeroHttpAgent.LOGGER.info(Info.HTTP_SERVERS, this.getClass().getSimpleName(), this.deploymentID(),
                    portLiteral);
            final List<Route> routes = router.getRoutes();
            final Map<String, Set<Route>> routeMap = new TreeMap<>();

            final Set<String> tree = new TreeSet<>();
            for (final Route route : routes) {
                // 2.Route
                if (null != route.getPath()) {
                    // Initial tree set.
                    if (!routeMap.containsKey(route.getPath())) {
                        routeMap.put(route.getPath(), new HashSet<>());
                    }
                    routeMap.get(route.getPath()).add(route);
                }
                // 3.Tree only need path here.
                final String path = null == route.getPath() ? "/*" : route.getPath();
                if (!"/*".equals(path)) {
                    tree.add(path);
                }
            }
            routeMap.forEach((path, routeSet) -> routeSet.forEach(route ->
                    ZeroHttpAgent.LOGGER.info(Info.MAPPED_ROUTE, this.getClass().getSimpleName(), path,
                            route.toString())));
            // 3. Endpoint Publish
            final String address =
                    MessageFormat.format("http://{0}:{1}/",
                            Ut.netIPv4(), portLiteral);
            ZeroHttpAgent.LOGGER.info(Info.HTTP_LISTEN, this.getClass().getSimpleName(), address);
            // 4. Send configuration to Event bus
            final String name = ZeroHttpAgent.SERVICES.get(port);
            this.startRegistry(name, options, tree);
        }
    }

    private void startRegistry(final String name,
                               final HttpServerOptions options,
                               final Set<String> tree) {
        // Enabled micro mode.
        if (ZeroHeart.isEtcd()) {
            final JsonObject data = this.getMessage(name, options, tree);
            // Send Data to Event Bus
            final EventBus bus = this.vertx.eventBus();
            final String address = ID.Addr.REGISTRY_START;
            ZeroHttpAgent.LOGGER.info(Info.MICRO_REGISTRY_SEND, this.getClass().getSimpleName(), name, address);
            bus.publish(address, data);
        }
    }

    private JsonObject getMessage(final String name,
                                  final HttpServerOptions options,
                                  final Set<String> tree) {
        final JsonObject data = new JsonObject();
        data.put(Registry.NAME, name);
        data.put(Registry.OPTIONS, options.toJson());
        // No Uri
        if (null != tree) {
            data.put(Registry.URIS, Ut.fromJoin(tree));
        }
        return data;
    }
}
