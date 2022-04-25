package io.vertx.up.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.up.annotations.Agent;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.ServerType;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.monitor.MeansureAxis;
import io.vertx.up.uca.options.DynamicVisitor;
import io.vertx.up.uca.options.ServerVisitor;
import io.vertx.up.uca.rs.Axis;
import io.vertx.up.uca.rs.router.PointAxis;
import io.vertx.up.uca.rs.router.RouterAxis;
import io.vertx.up.uca.rs.router.WallAxis;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Api Gateway for micro service architecture,
 * 1. Enable Service Discovery to search EndPoints
 * 2. Enable CircutBreaker to do breaker.
 */
@Agent(type = ServerType.API)
public class ZeroApiAgent extends AbstractVerticle {

    private static final Annal LOGGER = Annal.get(ZeroApiAgent.class);

    private static final ServerVisitor<HttpServerOptions> VISITOR =
        Ut.singleton(DynamicVisitor.class);
    private static final ConcurrentMap<Integer, AtomicInteger>
        API_START_LOGS = new ConcurrentHashMap<>();

    static {
        Fn.outUp(() -> {
            if (ZeroAtomic.API_OPTS.isEmpty()) {
                ZeroAtomic.API_OPTS.putAll(VISITOR.visit(ServerType.API.toString()));
                ZeroAtomic.API_OPTS.forEach((port, option) -> {
                    API_START_LOGS.put(port, new AtomicInteger(0));
                });
            }
        }, LOGGER);
    }

    @Override
    public void start() {
        /* 1.Call router hub to mount commont **/
        final Axis<Router> routerAxiser =
            Pool.CC_ROUTER.pick(() -> new RouterAxis(this.vertx), RouterAxis.class.getName());

        // Fn.po?lThread(Pool.ROUTERS, () -> new RouterAxis(this.vertx));
        /* 2.Call route hub to mount walls **/
        final Axis<Router> wallAxiser =
            Pool.CC_ROUTER.pick(() -> Ut.instance(WallAxis.class, this.vertx), WallAxis.class.getName());
        // Fn.po?lThread(Pool.WALLS, () -> Ut.instance(WallAxis.class, this.vertx));
        /* 3.Health route */
        final Axis<Router> montiorAxiser =
            Pool.CC_ROUTER.pick(() -> new MeansureAxis(this.vertx, false), MeansureAxis.class.getName() + "/" + true);

        // Fn.po?lThread(Pool.MEANSURES, () -> new MeansureAxis(this.vertx, true));
        Fn.outUp(() -> {

            // Set breaker for each server
            ZeroAtomic.API_OPTS.forEach((port, option) -> {
                /* Mount to api hub **/
                final Axis<Router> axiser = Fn.poolThread(Pool.APIS,
                    () -> Ut.instance(PointAxis.class, option, this.vertx));
                /* Single server processing **/
                final HttpServer server = this.vertx.createHttpServer(option);
                /* Router **/
                final Router router = Router.router(this.vertx);
                routerAxiser.mount(router);
                // Wall
                wallAxiser.mount(router);
                // Meansure
                montiorAxiser.mount(router);
                /* Api Logical **/
                axiser.mount(router);

                /* Listening **/
                server.requestHandler(router).listen();
                {
                    this.registryServer(option);
                }
            });
        }, LOGGER);
    }

    private void registryServer(final HttpServerOptions options) {
        final Integer port = options.getPort();
        final AtomicInteger out = API_START_LOGS.get(port);
        if (Values.ZERO == out.getAndIncrement()) {
            final String portLiteral = String.valueOf(port);
            LOGGER.info(Info.API_GATEWAY, this.getClass().getSimpleName(), this.deploymentID(),
                portLiteral);
            final String address =
                MessageFormat.format("http://{0}:{1}/",
                    options.getHost(), portLiteral);
            LOGGER.info(Info.API_LISTEN, this.getClass().getSimpleName(), address);
        }
    }
}
