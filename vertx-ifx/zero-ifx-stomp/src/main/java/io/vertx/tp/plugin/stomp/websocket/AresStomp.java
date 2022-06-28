package io.vertx.tp.plugin.stomp.websocket;

import io.vertx.core.SockOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.stomp.Destination;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.extension.AbstractAres;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.util.Ut;
import io.vertx.up.verticle.ZeroAtomic;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AresStomp extends AbstractAres {

    private static final Set<Remind> SOCKS = ZeroAnno.getSocks();


    public AresStomp(final Vertx vertx) {
        super(vertx);
    }

    @Override
    public void configure(final HttpServerOptions options) {
        super.configure(options);
        final SockOptions sockOptions = ZeroAtomic.SOCK_OPTS.get(options.getPort());
        Objects.requireNonNull(sockOptions);
        final HttpServerOptions configured = sockOptions.options();
        if (Objects.nonNull(configured)) {
            /* Re-define for WebSocket 8 attributes */
            options.setWebSocketAllowServerNoContext(configured.getWebSocketAllowServerNoContext());
            options.setWebSocketClosingTimeout(configured.getWebSocketClosingTimeout());
            options.setWebSocketCompressionLevel(configured.getWebSocketCompressionLevel());
            options.setWebSocketPreferredClientNoContext(configured.getWebSocketPreferredClientNoContext());
            /* Here must include stomp sub protocols */
            options.setWebSocketSubProtocols(configured.getWebSocketSubProtocols());

            options.setMaxWebSocketFrameSize(configured.getMaxWebSocketFrameSize());
            options.setMaxWebSocketMessageSize(configured.getMaxWebSocketMessageSize());
            options.setPerFrameWebSocketCompressionSupported(configured.getPerFrameWebSocketCompressionSupported());
            options.setPerMessageWebSocketCompressionSupported(configured.getPerMessageWebSocketCompressionSupported());
        }
    }

    @Override
    public void mount(final Router router, final JsonObject config) {
        final SockOptions sockOptions = ZeroAtomic.SOCK_OPTS.get(this.options.getPort());
        Objects.requireNonNull(sockOptions);
        final JsonObject stompJ = Ut.valueJObject(sockOptions.getConfig(), "stomp");
        final StompServerOptions stompOptions = new StompServerOptions(stompJ);
        final StompServer stompServer = StompServer.create(this.vertx(), stompOptions);
        // Iterator the SOCKS
        final StompServerHandler handler = StompServerHandler.create(this.vertx());
        // Grouped Socks
        handler.destinationFactory((v, name) -> {
            return Destination.topic(v, "Hi Hello");
        });
        SOCKS.forEach(sock -> {

        });
        // Build StompServer and bind webSocketHandler
        stompServer.handler(handler);
        this.server.webSocketHandler(stompServer.webSocketHandler());
    }
}
