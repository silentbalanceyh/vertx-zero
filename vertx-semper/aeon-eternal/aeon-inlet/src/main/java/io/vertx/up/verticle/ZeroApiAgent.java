package io.vertx.up.verticle;

import io.horizon.eon.VValue;
import io.horizon.eon.em.container.ServerType;
import io.horizon.uca.log.Annal;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.up.annotations.Agent;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroGrid;
import io.vertx.up.uca.monitor.MeasureAxis;
import io.vertx.up.uca.rs.Axis;
import io.vertx.up.uca.rs.router.PointAxis;
import io.vertx.up.uca.rs.router.RouterAxis;
import io.vertx.up.uca.rs.router.WallAxis;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Api Gateway for micro service architecture,
 * 1. Enable Service Discovery to search EndPoints
 * 2. Enable CircutBreaker to do breaker.
 */
@Agent(type = ServerType.API)
public class ZeroApiAgent extends AbstractVerticle {

    private static final Annal LOGGER = Annal.get(ZeroApiAgent.class);

    @Override
    public void start() {
        /* 1.Call router hub to mount commont **/
        final Axis<Router> routerAxiser =
            CACHE.CC_ROUTER.pick(() -> new RouterAxis(this.vertx), RouterAxis.class.getName());

        // Fn.po?lThread(Pool.ROUTERS, () -> new RouterAxis(this.vertx));
        /* 2.Call route hub to mount walls **/
        final Axis<Router> wallAxiser =
            CACHE.CC_ROUTER.pick(() -> Ut.instance(WallAxis.class, this.vertx), WallAxis.class.getName());
        // Fn.po?lThread(Pool.WALLS, () -> Ut.instance(WallAxis.class, this.vertx));
        /* 3.Health route */
        final Axis<Router> montiorAxiser =
            CACHE.CC_ROUTER.pick(() -> new MeasureAxis(this.vertx, false), MeasureAxis.class.getName() + "/" + true);

        Fn.outBug(() -> ZeroGrid.getGatewayOptions().forEach((port, option) -> {
            /* Mount to api hub **/
            final Axis<Router> axiser = CACHE.CC_ROUTER.pick(
                () -> Ut.instance(PointAxis.class, option, this.vertx), PointAxis.class.getName());
            // Fn.po?lThread(Pool.APIS, () -> Ut.instance(PointAxis.class, option, this.vertx));
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
        }), LOGGER);
    }

    private void registryServer(final HttpServerOptions options) {
        final Integer port = options.getPort();
        final AtomicInteger out = ZeroGrid.ATOMIC_LOG.get(port);
        if (VValue.ZERO == out.getAndIncrement()) {
            final String portLiteral = String.valueOf(port);
            LOGGER.info(INFO.ZeroApiAgent.API_GATEWAY, this.getClass().getSimpleName(), this.deploymentID(),
                portLiteral);
            final String address =
                MessageFormat.format("http://{0}:{1}/",
                    options.getHost(), portLiteral);
            LOGGER.info(INFO.ZeroApiAgent.API_LISTEN, this.getClass().getSimpleName(), address);
        }
    }
}
