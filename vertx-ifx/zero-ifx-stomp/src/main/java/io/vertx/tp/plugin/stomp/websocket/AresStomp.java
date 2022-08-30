package io.vertx.tp.plugin.stomp.websocket;

import io.vertx.core.SockOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.tp.plugin.stomp.socket.ServerWsHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.extension.AbstractAres;
import io.vertx.up.runtime.ZeroGrid;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AresStomp extends AbstractAres {


    public AresStomp(final Vertx vertx) {
        super(vertx);
    }

    @Override
    public void configure(final HttpServerOptions options) {
        super.configure(options);
        final SockOptions sockOptions = ZeroGrid.getSockOptions().get(options.getPort());
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
        final SockOptions sockOptions = ZeroGrid.getSockOptions().get(this.options.getPort());
        Objects.requireNonNull(sockOptions);
        final JsonObject stompJ = Ut.valueJObject(sockOptions.getConfig(), "stomp");
        final StompServerOptions stompOptions = new StompServerOptions(stompJ);
        final StompServer stompServer = StompServer.create(this.vertx(), stompOptions);
        // Iterator the SOCKS
        final ServerWsHandler handler = ServerWsHandler.create(this.vertx());

        {
            // Security for WebSocket
            final Mixer mAuthorize =
                Mixer.instance(MixerAuthorize.class, this.vertx());
            final Aegis aegis = mAuthorize.mount(handler, stompOptions);

            // Mount user definition handler
            final Mixer mHandler =
                Mixer.instance(MixerHandler.class, this.vertx(), aegis);
            mHandler.mount(handler);

            // Mount event bus
            final Mixer mBridge =
                Mixer.instance(MixerBridge.class, this.vertx());
            mBridge.mount(handler, stompOptions);

            // Mount destination
            final Mixer mDestination =
                Mixer.instance(MixerDestination.class, this.vertx());
            mDestination.mount(handler);
        }

        // Build StompServer and bind webSocketHandler
        stompServer.handler(handler);
        this.server.webSocketHandler(stompServer.webSocketHandler());
    }
}
