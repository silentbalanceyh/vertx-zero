package io.vertx.up.uca.rs.hunt;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.commune.Envelop;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.uca.rs.Aim;
import io.vertx.up.util.Ut;
import io.vertx.up.fn.Fn;

/**
 * BlockAim: Non-Event Bus: One-Way
 */
public class PingAim extends BaseAim implements Aim<RoutingContext> {

    @Override
    public Handler<RoutingContext> attack(final Event event) {
        return Fn.getNull(() -> (context) -> this.exec(() -> {
            // 1. Build TypedArgument
            final Object[] arguments = this.buildArgs(context, event);

            // 2. Method call
            final Object invoked = this.invoke(event, arguments);
            // 3. Resource model building
            final Envelop data;
            if (Ut.isBoolean(invoked)) {
                data = Envelop.success(invoked);
            } else {
                data = Envelop.success(Boolean.TRUE);
            }
            // 4. Process modal
            Answer.reply(context, data, event);
        }, context, event), event);
    }
}
