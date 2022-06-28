package io.vertx.up.extension.router;

import io.vertx.core.SockOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.up.eon.KName;
import io.vertx.up.extension.AbstractAres;
import io.vertx.up.extension.Ares;
import io.vertx.up.extension.router.websocket.AresSockJs;
import io.vertx.up.util.Ut;
import io.vertx.up.verticle.ZeroAtomic;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AresWs extends AbstractAres {

    private final Ares publish;
    private Ares executor;
    private SockOptions sockOptions;

    AresWs(final Vertx vertx) {
        super(vertx);
        this.publish = new AresSockJs(vertx);
    }

    @Override
    public void configure(final HttpServerOptions options) {
        super.configure(options);
        // Pre-Configuration of HttpServerOptions for specific usage
        final SockOptions sockOptions = ZeroAtomic.SOCK_OPTS.getOrDefault(options.getPort(), null);
        this.sockOptions = sockOptions;

        // 1. The default component implementation class for publish only ( Non Secure )
        final Class<?> aresCls = Objects.isNull(sockOptions.getComponent()) ? AresSockJs.class : sockOptions.getComponent();
        // 2. Build reference of component of Ares
        final Ares ares = Ut.instance(aresCls, this.vertx());
        ares.configure(options);

        this.executor = ares;
    }

    /*
     * WebSocket Extension for user-defined WebSocket server that be connected
     * to current HTTP server by `port`. Here the WebSocket configuration should share
     * HTTP Port instead of standalone. it means that when you configure the WebSocket
     * in your environment, the port number must be the same as HTTP server, if the port
     * does not exist in your HTTP server instances, the WebSocket server will be
     * ignored.
     */
    @Override
    public void mount(final Router router, final JsonObject config) {
        /*
         * 1. Whether the `publish` has been enabled
         */
        final String publish = this.sockOptions.getPublish();
        if (Ut.notNil(publish)) {
            final JsonObject configSockJs = this.sockOptions.configSockJs();
            this.publish.mount(router, configSockJs);
        }


        /*
         * 2. Major executor will be triggered
         */
        final JsonObject configuration = new JsonObject();
        configuration.put(KName.CONFIG, Ut.valueJObject(this.sockOptions.getConfig()));
        configuration.put(KName.SERVER, Ut.valueJObject(this.sockOptions.getServer()));
        this.executor.bind(this.server, this.options).mount(router, configuration);
    }

}
