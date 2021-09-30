package io.vertx.up.secure.handler;

import io.vertx.core.Vertx;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.fn.Fn;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Security Module for dispatcher,
 * Build standard AuthHandler for different workflow.
 */
public interface Bolt {

    ConcurrentMap<String, Bolt> POOL = new ConcurrentHashMap<>();

    static Bolt get() {
        return Fn.poolThread(POOL, BoltBridge::new, BoltBridge.class.getName());
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
