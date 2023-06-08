package io.vertx.mod.jet.uca.aim;

import io.horizon.spi.jet.JtIngest;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.up.backbone.hunt.Answer;
import io.vertx.up.commune.Envelop;

/**
 * The first handler in routing
 * 1. Host http header checking ( Not Support ）
 * 2. Analyzing required part ( required / contained )
 * 3. Analyzing parameters of api, get parameters and build `Envelop`
 */
public class PreAim implements JtAim {
    @Override
    public Handler<RoutingContext> attack(final JtUri uri) {
        /* JtIngest in booting workflow instead of other position */
        final JtIngest ingest = JtIngest.getInstance();
        return context -> {
            /* Extract parameters */
            final Envelop envelop = ingest.in(context, uri);
            /* Last step for next */
            Answer.next(context, envelop);
        };
    }
}
