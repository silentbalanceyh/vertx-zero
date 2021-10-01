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
 *
 * 1. Native: Vertx Standard
 * 2. Extension: User Defined
 */
class BoltBridge implements Bolt {

    private final transient Bolt internal;
    private final transient Bolt extension;

    BoltBridge() {
        this.internal = Fn.poolThread(POOL, BoltNative::new, BoltNative.class.getName());
        this.extension = Fn.poolThread(POOL, BoltExtension::new, BoltExtension.class.getName());
    }

    /*
     * 401
     */
    @Override
    public AuthenticationHandler authorize(final Vertx vertx, final Aegis aegis) {
        Objects.requireNonNull(aegis);
        if (aegis.okForAuthorize()) {
            if (aegis.isDefined()) {
                return this.extension.authorize(vertx, aegis);
            } else {
                return this.internal.authorize(vertx, aegis);
            }
        } else {
            // The condition is invalid, could not generate handler
            return null;
        }
    }

    /*
     * 403
     */
    @Override
    public AuthorizationHandler access(final Vertx vertx, final Aegis aegis) {
        Objects.requireNonNull(aegis);
        if (aegis.okForAccess()) {
            if (aegis.isDefined()) {
                return this.extension.access(vertx, aegis);
            } else {
                return this.internal.access(vertx, aegis);
            }
        } else {
            // The condition is invalid, could not generate handler
            return null;
        }
    }
}
