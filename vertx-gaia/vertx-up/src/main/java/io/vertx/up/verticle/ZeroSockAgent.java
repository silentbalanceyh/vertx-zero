package io.vertx.up.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.up.annotations.Agent;
import io.vertx.up.eon.em.ServerType;
import io.vertx.up.log.Annal;

/**
 * Default websocket server agent for router handlers.
 * Once you have defined another agent, the default agent will be replaced.
 * Recommend: Do not modify any agent that vertx zero provided.
 */
@Agent(type = ServerType.SOCK)
public class ZeroSockAgent extends AbstractVerticle {
    private static final Annal LOGGER = Annal.get(ZeroSockAgent.class);

    @Override
    public void start() {
        // Server Listen
        ZeroAtomic.SOCK_OPTS.forEach((port, option) -> {
            /* Create Server **/
            final HttpServer server = this.vertx.createHttpServer(option);

            final Router router = Router.router(this.vertx);

            /* Handler **/
            server.requestHandler(router).listen();
        });
    }
}
