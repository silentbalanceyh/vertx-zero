package io.vertx.up.plugin.stomp.websocket;

import io.vertx.core.Vertx;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MixerBridge extends AbstractMixer {

    public MixerBridge(final Vertx vertx) {
        super(vertx);
    }

    @Override
    public <T> T mount(final StompServerHandler handler, final StompServerOptions options) {
        handler.bridge(BridgeStomp.wsOptionBridge());
        return this.finished();
    }
}
