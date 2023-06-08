package io.vertx.up.backbone;

import io.vertx.core.Handler;
import io.vertx.up.atom.agent.Depot;

/**
 * JSR330 signal
 */
public interface Sentry<Context> {
    /**
     * @param depot Depot instance for before validator
     *
     * @return Handler for Context
     */
    Handler<Context> signal(final Depot depot);
}
