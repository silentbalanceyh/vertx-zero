package io.vertx.tp.plugin.stomp.websocket;

import io.vertx.core.SockOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.extension.AbstractAres;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.verticle.ZeroAtomic;

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
    public void mount(final Router router, final JsonObject config) {
        final SockOptions sockOptions = ZeroAtomic.SOCK_OPTS.get(this.options.getPort());
        // Iterator the SOCKS
        SOCKS.forEach(sock -> {

        });
    }
}
