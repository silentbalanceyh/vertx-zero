package io.vertx.up.extension.router;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.up.extension.AbstractAres;
import io.vertx.up.extension.Ares;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AresHub extends AbstractAres {
    private final Ares dynamic;

    private final Ares wSocket;

    public AresHub(final Vertx vertxRef) {
        super(vertxRef);
        this.dynamic = new AresDynamic(vertxRef);
        this.wSocket = new AresWs(vertxRef);
    }

    @Override
    public void configure(final HttpServerOptions options) {
        super.configure(options);
        /*
         * Only this reference need configure method to
         * modify the reference of HttpServerOptions before
         * create server.
         */
        this.wSocket.configure(options);
    }

    @Override
    public void mount(final Router router, final JsonObject config) {
        /*
         * Dynamic Extension for some user-defined router to resolve some spec
         * requirement such as Data Driven System and Origin X etc.
         * Call second method to inject vertx reference.
         *
         * This configuration will be in `tp` part such as:
         *
         * In file: vertx-jet.yml
         *
         * router:
         *    wall: /api
         *    worker:
         *       instances: 64
         *    agent:
         *       instances: 32
         *    unity: io.horizon.spi.environment.UnityAmbient
         *
         * In file: vertx-inject.yml
         * router: io.vertx.mod.jet.JetPollux # ( zero-atom module )
         *
         */
        this.dynamic.bind(this.server, this.options).mount(router);

        /*
         * This configuration is for websocket in server configuration.
         *
         * In file: vertx-server.yml
         * server:
         * - name: ht-ws
         *   type: sock
         *   config:
         *     port: 7085       # HttpServerOptions, this port could be the same as HTTP definition
         *   websocket:
         *     publish:
         *     component:
         *     config:
         *        stomp:
         *        bridge:
         *        handler:
         *     server:         # Whether Enable Sock Server / Stomp Server
         */
        this.wSocket.bind(this.server, this.options).mount(router);
    }
}
