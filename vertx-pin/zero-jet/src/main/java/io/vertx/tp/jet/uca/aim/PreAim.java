package io.vertx.tp.jet.uca.aim;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.tp.jet.atom.JtUri;
import io.vertx.tp.optic.jet.JtIngest;
import io.vertx.up.commune.Envelop;
import io.vertx.up.uca.rs.hunt.Answer;

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
            /* Tenant injection */
            envelop.tenant();
            /* Last step for next */
            Answer.next(context, envelop);
        };
    }
}
