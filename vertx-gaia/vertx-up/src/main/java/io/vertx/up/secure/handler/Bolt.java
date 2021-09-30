package io.vertx.up.secure.handler;

import io.vertx.core.Vertx;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Aegis;

/**
 * Security Module for dispatcher,
 * Build standard AuthHandler for different workflow.
 */
public interface Bolt {

    static Bolt get() {
        return BoltBridge.create();
    }

    /*
     * Authentication
     */
    AuthenticationHandler authorize(final Vertx vertx,
                                    final Aegis aegis);

    /*
     * Authorization
     */
    AuthorizationHandler access(final Vertx vertx,
                                final Aegis aegis);
}
