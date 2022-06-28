package io.vertx.up.extension.router;

import io.vertx.core.SockOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.extension.AbstractAres;
import io.vertx.up.extension.Ares;
import io.vertx.up.extension.router.websocket.AresBridge;
import io.vertx.up.extension.router.websocket.AresSockJs;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.util.Ut;
import io.vertx.up.verticle.ZeroAtomic;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AresWs extends AbstractAres {


    private static final Set<Remind> SOCKS = ZeroAnno.getSocks();
    private static final boolean ENABLED = !ZeroAtomic.SOCK_OPTS.isEmpty() && !SOCKS.isEmpty();
    private final Ares publish;
    private Ares executor;
    private SockOptions sockOptions;

    AresWs(final Vertx vertx) {
        super(vertx);
        this.publish = new AresSockJs(vertx);
    }

    @Override
    public void configure(final HttpServerOptions options) {
        if (ENABLED) {
            super.configure(options);
            // Pre-Configuration of HttpServerOptions for specific usage
            this.sockOptions = ZeroAtomic.SOCK_OPTS.getOrDefault(options.getPort(), null);
            if (Objects.nonNull(this.sockOptions)) {
                final String publish = this.sockOptions.getPublish();
                if (Ut.notNil(publish)) {
                    this.publish.configure(options);
                    this.logger().info(Info.WS_PUBLISH, String.valueOf(options.getPort()),
                        options.getHost(), publish);
                }

                // 1. The default component implementation class for publish only ( Non Secure )
                final Class<?> aresCls = Objects.isNull(this.sockOptions.getComponent()) ? AresBridge.class : this.sockOptions.getComponent();
                this.logger().info(Info.WS_COMPONENT, aresCls, this.sockOptions.getComponent());

                // 2. Build reference of component of Ares
                this.executor = Ut.instance(aresCls, this.vertx());
                this.executor.configure(options);
            }
        } else {
            this.logger().info(Info.WS_DISABLED, String.valueOf(options.getPort()), options.getHost());
        }
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
        if (ENABLED && Objects.nonNull(this.sockOptions)) {
            /*
             * 1. Whether the `publish` has been enabled
             */
            final String publish = this.sockOptions.getPublish();
            if (Ut.notNil(publish)) {
                this.publish.mount(router, this.sockOptions.configSockJs());
            }
            /*
             * 2. Major executor will be triggered
             */
            this.executor.bind(this.server, this.options).mount(router);
        }
    }

}
