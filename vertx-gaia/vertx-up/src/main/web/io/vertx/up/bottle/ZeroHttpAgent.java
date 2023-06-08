package io.vertx.up.bottle;

import io.horizon.eon.VName;
import io.horizon.eon.VValue;
import io.horizon.specification.boot.HAxis;
import io.horizon.uca.log.Annal;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.up.annotations.Agent;
import io.vertx.up.backbone.router.EventAxis;
import io.vertx.up.backbone.router.FilterAxis;
import io.vertx.up.backbone.router.RouterAxis;
import io.vertx.up.backbone.router.WallAxis;
import io.vertx.up.configuration.BootStore;
import io.vertx.up.eon.KWeb;
import io.vertx.up.eon.em.Etat;
import io.vertx.up.extension.Ares;
import io.vertx.up.runtime.ZeroOption;
import io.vertx.up.uca.monitor.MeasureAxis;
import io.vertx.up.uca.registry.Uddi;
import io.vertx.up.uca.registry.UddiRegistry;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Default Http Server agent for router handlers.
 * Once you have defined another Agent, the default agent will be replaced.
 * Recommend: Do not modify any agent that vertx zero provided.
 */
@Agent
public class ZeroHttpAgent extends AbstractVerticle {

    private static final BootStore STORE = BootStore.singleton();

    private static final Annal LOGGER = Annal.get(ZeroHttpAgent.class);

    private static final ConcurrentMap<Integer, String> SERVICES =
        ZeroOption.getServerNames();

    @Override
    public void start() {
        /* 1.Call router hub to mount common **/
        final HAxis<Router> routerAxis = CACHE.CC_ROUTER.pick(
            () -> new RouterAxis(this.vertx), RouterAxis.class.getName());
        // Fn.po?lThread(Pool.ROUTERS, () -> new RouterAxis(this.vertx));

        /* 2.Call route hub to mount defined **/
        final HAxis<Router> axis = CACHE.CC_ROUTER.pick(
            EventAxis::new, EventAxis.class.getName());

        /* 3.Call route hub to mount walls **/
        final HAxis<Router> wallAxis = CACHE.CC_ROUTER.pick(
            () -> Ut.instance(WallAxis.class, this.vertx), WallAxis.class.getName());
        // Fn.po?lThread(Pool.WALLS, () -> Ut.instance(WallAxis.class, this.vertx));


        /* 4.Call route hub to mount filters **/
        final HAxis<Router> filterAxis = CACHE.CC_ROUTER.pick(
            FilterAxis::new, FilterAxis.class.getName());
        // Fn.po?lThread(Pool.FILTERS, FilterAxis::new);


        /* 5.Call route to mount Measure **/
        final HAxis<Router> monitorAxis = CACHE.CC_ROUTER.pick(
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
        ZeroOption.getServerOptions().forEach((port, option) -> {
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
        Ut.itMap(ZeroOption.getServerOptions(), (port, config) -> {
            // Enabled micro mode.
            if (STORE.isEtcd()) {
                // Template call registry to modify the status of current service.
                final UddiRegistry registry = Uddi.registry(this.getClass());
                registry.registryHttp(ZeroHttpAgent.SERVICES.get(port), config, Etat.STOPPED);
            }
        });
    }

    private void registryServer(final HttpServerOptions options,
                                final Router router) {
        final Integer port = options.getPort();
        final AtomicInteger out = ZeroOption.ATOMIC_LOG.get(port);
        if (VValue.ZERO == out.getAndIncrement()) {
            // 1. Build logs for current server;
            final String portLiteral = String.valueOf(port);
            LOGGER.info(INFO.ZeroHttpAgent.HTTP_SERVERS, this.getClass().getSimpleName(), this.deploymentID(),
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
                LOGGER.info(INFO.ZeroHttpAgent.MAPPED_ROUTE, this.getClass().getSimpleName(), path,
                    this.optionRegistry(route))));
            // 3. Endpoint Publish
            final String address =
                MessageFormat.format("http://{0}:{1}/",
                    Ut.netIPv4(), portLiteral);
            LOGGER.info(INFO.ZeroHttpAgent.HTTP_LISTEN, this.getClass().getSimpleName(), address);
            // 4. Send configuration to Event bus
            final String name = ZeroHttpAgent.SERVICES.get(port);
            this.startRegistry(name, options, tree);
        }
    }

    private String optionRegistry(final Route route) {
        return Ut.fromJoin(Optional.ofNullable(route.methods()).orElse(new HashSet<>())
            .stream().map(HttpMethod::name).collect(Collectors.toSet()));
    }

    private void startRegistry(final String name,
                               final HttpServerOptions options,
                               final Set<String> tree) {
        // Enabled micro mode.
        if (STORE.isEtcd()) {
            final JsonObject data = this.getMessage(name, options, tree);
            // Send Data to Event Bus
            final EventBus bus = this.vertx.eventBus();
            final String address = KWeb.ADDR.EBS_REGISTRY_START;
            LOGGER.info(INFO.ZeroHttpAgent.MICRO_REGISTRY_SEND, this.getClass().getSimpleName(), name, address);
            bus.publish(address, data);
        }
    }

    private JsonObject getMessage(final String name,
                                  final HttpServerOptions options,
                                  final Set<String> tree) {
        final JsonObject data = new JsonObject();
        data.put(VName.NAME, name);
        data.put(VName.OPTIONS, options.toJson());
        // No Uri
        if (null != tree) {
            data.put(VName.URIS, Ut.fromJoin(tree));
        }
        return data;
    }
}
