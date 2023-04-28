package io.vertx.up.verticle;

import io.horizon.eon.VValue;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.up.annotations.Agent;
import io.vertx.up.eon.KWeb;
import io.vertx.up.eon.em.Etat;
import io.vertx.up.extension.Ares;
import io.horizon.uca.log.Annal;
import io.vertx.up.runtime.ZeroGrid;
import io.vertx.up.runtime.ZeroHeart;
import io.vertx.up.uca.monitor.MeasureAxis;
import io.vertx.up.uca.registry.Uddi;
import io.vertx.up.uca.registry.UddiRegistry;
import io.vertx.up.uca.rs.Axis;
import io.vertx.up.uca.rs.router.EventAxis;
import io.vertx.up.uca.rs.router.FilterAxis;
import io.vertx.up.uca.rs.router.RouterAxis;
import io.vertx.up.uca.rs.router.WallAxis;
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
        /* 1.Call router hub to mount common **/
        final Axis<Router> routerAxis = Pool.CC_ROUTER.pick(
            () -> new RouterAxis(this.vertx), RouterAxis.class.getName());
        // Fn.po?lThread(Pool.ROUTERS, () -> new RouterAxis(this.vertx));

        /* 2.Call route hub to mount defined **/
        final Axis<Router> axis = Pool.CC_ROUTER.pick(
            EventAxis::new, EventAxis.class.getName());

        /* 3.Call route hub to mount walls **/
        final Axis<Router> wallAxis = Pool.CC_ROUTER.pick(
            () -> Ut.instance(WallAxis.class, this.vertx), WallAxis.class.getName());
        // Fn.po?lThread(Pool.WALLS, () -> Ut.instance(WallAxis.class, this.vertx));


        /* 4.Call route hub to mount filters **/
        final Axis<Router> filterAxis = Pool.CC_ROUTER.pick(
            FilterAxis::new, FilterAxis.class.getName());
        // Fn.po?lThread(Pool.FILTERS, FilterAxis::new);


        /* 5.Call route to mount Measure **/
        final Axis<Router> monitorAxis = Pool.CC_ROUTER.pick(
            () -> new MeasureAxis(this.vertx, false), MeasureAxis.class.getName() + "/" + false);
        // Fn.po?lThread(Pool.MEANSURES, () -> new MeansureAxis(this.vertx, false));

        /*
         * New Extension Structure, Here I designed new interface `Ares` for router extension Part to replace
         * previous system.
         * Here the framework use the structure of following:
         * 1. The instance type is `AresHub`
         * 2. Internal Call chain is as following:
         * -- 2.1. AresDynamic / It's for dynamic routing system.
         * -- 2.2. AresSock    / It's for websocket system ( with STOMP or not ).
         */
        final Ares ares = Ares.instance(this.vertx);

        /* Get the default HttpServer Options **/
        ZeroGrid.getServerOptions().forEach((port, option) -> {
            /*
             * To enable extend of StompServer, there should be
             * Some code logical to set WebSocket Sub Protocols such as
             *
             * v10.stomp, v11.stomp, v12.stomp etc, instead, when you create
             * HttpServer, there should be some code logical to cross configuration
             **/
            ares.configure(option);

            /* Single server processing **/
            final HttpServer server = this.vertx.createHttpServer(option);

            /* Build router with current option **/
            final Router router = Router.router(this.vertx);

            // Router
            routerAxis.mount(router);
            // Wall
            wallAxis.mount(router);
            // Event
            axis.mount(router);
            // Measure
            monitorAxis.mount(router);

            /*
             * Extension Part of New, here the called API is empty `config`, second parameter is
             * {} because all the component config data will be passed internal each component
             * in `Hub` instead of input here.
             */
            ares.bind(server, option).mount(router);


            // Filter
            filterAxis.mount(router);
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
        Ut.itMap(ZeroGrid.getServerOptions(), (port, config) -> {
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
        final AtomicInteger out = ZeroGrid.ATOMIC_LOG.get(port);
        if (VValue.ZERO == out.getAndIncrement()) {
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
            final String address = KWeb.ADDR.EBS_REGISTRY_START;
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
