package io.vertx.up.bottle;

import io.horizon.eon.VValue;
import io.horizon.specification.boot.HAxis;
import io.horizon.uca.log.Annal;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.up.annotations.Agent;
import io.vertx.up.backbone.router.PointAxis;
import io.vertx.up.backbone.router.RouterAxis;
import io.vertx.up.backbone.router.WallAxis;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroOption;
import io.vertx.up.uca.monitor.MeasureAxis;
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
        final HAxis<Router> routerAxiser =
            CACHE.CC_ROUTER.pick(() -> new RouterAxis(this.vertx), RouterAxis.class.getName());

        // Fn.po?lThread(Pool.ROUTERS, () -> new RouterAxis(this.vertx));
        /* 2.Call route hub to mount walls **/
        final HAxis<Router> wallAxiser =
            CACHE.CC_ROUTER.pick(() -> Ut.instance(WallAxis.class, this.vertx), WallAxis.class.getName());
        // Fn.po?lThread(Pool.WALLS, () -> Ut.instance(WallAxis.class, this.vertx));
        /* 3.Health route */
        final HAxis<Router> montiorAxiser =
            CACHE.CC_ROUTER.pick(() -> new MeasureAxis(this.vertx, false), MeasureAxis.class.getName() + "/" + true);

        Fn.outBug(() -> ZeroOption.getGatewayOptions().forEach((port, option) -> {
            /* Mount to api hub **/
            final HAxis<Router> axiser = CACHE.CC_ROUTER.pick(
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
        final AtomicInteger out = ZeroOption.ATOMIC_LOG.get(port);
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
