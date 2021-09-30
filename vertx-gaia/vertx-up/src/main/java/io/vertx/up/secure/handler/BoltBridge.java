package io.vertx.up.secure.handler;

import io.vertx.core.Vertx;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Aegis;

import java.util.Objects;

/**
 * Dispatch Bolt to real bolt instance.
 */
class BoltBridge implements Bolt {

    private static Bolt INSTANCE;
    private final transient Bolt internal;

    private BoltBridge() {
        this.internal = BoltNative.create();
    }

    static Bolt create() {
        if (null == INSTANCE) {
            INSTANCE = new BoltBridge();
        }
        return INSTANCE;
    }

    @Override
    public AuthenticationHandler authorize(final Vertx vertx, final Aegis aegis) {
        Objects.requireNonNull(aegis);
        if (aegis.isDefined()) {
            // TODO: Defined - Authenticate
            return null;
        } else {
            return this.internal.authorize(vertx, aegis);
        }
    }

    @Override
    public AuthorizationHandler access(final Vertx vertx, final Aegis aegis) {
        Objects.requireNonNull(aegis);
        if (aegis.isDefined()) {
            // TODO: Defined - Authorize
            return null;
        } else {
            return this.internal.access(vertx, aegis);
        }
    }
}
