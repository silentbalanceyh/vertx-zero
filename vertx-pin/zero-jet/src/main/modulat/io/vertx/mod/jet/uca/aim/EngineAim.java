package io.vertx.mod.jet.uca.aim;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.mod.jet.monitor.JtMonitor;
import io.vertx.up.backbone.hunt.Answer;
import io.vertx.up.commune.Envelop;

/*
 * Routing engine generation for Script Engine.
 */
public class EngineAim implements JtAim {
    private transient final JtMonitor monitor = JtMonitor.create(this.getClass());

    @Override
    public Handler<RoutingContext> attack(final JtUri uri) {
        return context -> {
            /*
             * Async log information in this step
             */
            final Envelop request = Answer.previous(context);
            final JsonObject data = request.data();
            this.monitor.aimEngine(uri.method(), uri.path(), data);
            /*
             * Next step
             * Resolution for next step data stored into envelop
             */
            Answer.next(context, request);
        };
    }
}
