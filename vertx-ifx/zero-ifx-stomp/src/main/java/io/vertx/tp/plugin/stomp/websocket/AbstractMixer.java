package io.vertx.tp.plugin.stomp.websocket;

import io.vertx.core.Vertx;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.stomp.BridgeOptions;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
abstract class AbstractMixer implements Mixer {

    protected Vertx vertx;

    AbstractMixer(final Vertx vertx) {
        this.vertx = vertx;
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    @SuppressWarnings("unchecked")
    protected <T> T finished(final Object returned) {
        if (Objects.isNull(returned)) {
            return null;
        } else {
            return (T) returned;
        }
    }

    protected <T> T finished() {
        return this.finished(null);
    }

    protected BridgeOptions bridge(final Set<Remind> sockOk) {
        final BridgeOptions bridgeOptions = new BridgeOptions();
        sockOk.forEach(remind -> {
            if (Ut.notNil(remind.getAddress()) && Ut.notNil(remind.getSubscribe())) {
                // From Event Bus
                final PermittedOptions from = new PermittedOptions();
                from.setAddress(remind.getAddress());
                // To Stomp
                final PermittedOptions to = new PermittedOptions();
                to.setAddress(remind.getSubscribe());
                bridgeOptions.addInboundPermitted(from).addOutboundPermitted(to);
            }
        });
        return bridgeOptions;
    }
}
