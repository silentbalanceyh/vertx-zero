package io.vertx.up.backbone;

import io.vertx.core.Handler;
import io.vertx.up.atom.agent.Event;

/**
 * Hunt to aim and select the objective
 */
public interface Aim<Context> {
    /**
     * @param event Scanned Event definition here
     *
     * @return Handler for `RoutingContext`
     */
    Handler<Context> attack(final Event event);
}
