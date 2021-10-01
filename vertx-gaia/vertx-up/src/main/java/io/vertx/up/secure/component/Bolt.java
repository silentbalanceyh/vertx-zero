package io.vertx.up.secure.component;

import io.vertx.core.Vertx;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.fn.Fn;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Security Module for dispatcher,
 * Build standard AuthHandler for different workflow here
 *
 * 1. Level 1
 * Here are two kinds of security module: NATIVE / EXTENSION
 * For NATIVE, zero framework use Vertx native handlers of security module
 * For EXTENSION, zero framework will find actual handler by configuration of `key` part
 *
 * 2. Level 2
 * For authenticate workflow ( 401 ), when the system detect multi Aegis, the whole handlers
 * will be used in `Chain` mode instead of single one
 * But for authorization workflow ( 403 ), the system will find the only one handler based on
 * the configuration. ( Current version will compare order that whose order is small )
 *
 * *: The best practice is that you define 1 - 1 mode, if you want to more than one wall, you
 * can define only one wall annotated by @Authorization ( 403 )
 */
public interface Bolt {

    ConcurrentMap<String, Bolt> POOL = new ConcurrentHashMap<>();

    static Bolt get() {
        return Fn.poolThread(POOL, BoltBridge::new, BoltBridge.class.getName());
    }

    /*
     * Authentication
     */
    AuthenticationHandler authorize(Vertx vertx, Aegis aegis);

    /*
     * Authorization
     */
    AuthorizationHandler access(Vertx vertx, Aegis aegis);
}
