package io.vertx.tp.plugin.stomp.websocket;

import io.vertx.core.Vertx;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.tp.plugin.stomp.command.FrameWsHandler;
import io.vertx.up.atom.secure.Aegis;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MixerHandler extends AbstractMixer {
    private transient final Aegis aegis;

    public MixerHandler(final Vertx vertx, final Aegis aegis) {
        super(vertx);
        this.aegis = aegis;
    }

    @Override
    public <T> T mount(final StompServerHandler handler, final StompServerOptions options) {
        // Replace Connect Handler because of Security Needed.
        final FrameWsHandler connectHandler = FrameWsHandler.connector(this.vertx);
        handler.connectHandler(connectHandler.bind(this.aegis));
        return null;
    }
}
