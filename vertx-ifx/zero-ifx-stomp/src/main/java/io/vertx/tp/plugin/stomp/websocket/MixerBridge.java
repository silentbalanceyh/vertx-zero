package io.vertx.tp.plugin.stomp.websocket;

import io.vertx.core.Vertx;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.up.atom.worker.Remind;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MixerBridge extends AbstractMixer {
    private final transient Set<Remind> sockOk = new HashSet<>();

    public MixerBridge(final Vertx vertx, final Set<Remind> sockOk) {
        super(vertx);
        if (Objects.nonNull(sockOk)) {
            this.sockOk.addAll(sockOk);
        }
    }

    @Override
    public <T> T mount(final StompServerHandler handler, final StompServerOptions options) {
        handler.bridge(this.bridge(this.sockOk));
        return this.finished();
    }
}
