package io.vertx.up.secure.component;

import io.vertx.core.Vertx;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.fn.Fn;

import java.util.Objects;

/**
 * Dispatch Bolt to real bolt instance, here are the bridge
 * between standard and user defined part
 */
class BoltBridge implements Bolt {

    private final transient Bolt internal;

    BoltBridge() {
        this.internal = Fn.poolThread(POOL, BoltNative::new, BoltNative.class.getName());
    }

    @Override
    public AuthenticationHandler authorize(final Vertx vertx, final Aegis aegis) {
        Objects.requireNonNull(aegis);
        if (aegis.okForAuthorize()) {
            if (aegis.isDefined()) {
                // TODO: Defined - Authenticate
                return null;
            } else {
                return this.internal.authorize(vertx, aegis);
            }
        } else {
            // The condition is invalid
            return null;
        }
    }

    @Override
    public AuthorizationHandler access(final Vertx vertx, final Aegis aegis) {
        Objects.requireNonNull(aegis);
        if (aegis.okForAccess()) {
            if (aegis.isDefined()) {
                // TODO: Defined - Access
                return null;
            } else {
                return this.internal.access(vertx, aegis);
            }
        } else {
            // The condition is invalid
            return null;
        }
    }
}
