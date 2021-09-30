package io.vertx.up.secure.handler;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Native AuthHandler extract from bolt,
 * All native authHandler could be bind to vert.x security module
 */
class BoltNative implements Bolt {

    /*
     *  Extract AuthenticationHandler
     */
    @Override
    public AuthenticationHandler authorize(final Vertx vertx,
                                           final Aegis aegis) {
        Objects.requireNonNull(aegis);
        if (aegis.okForAuthorize()) {
            return Fn.getJvm(() -> {
                // Config Extracting
                final JsonObject config = Ut.sureJObject(aegis.getConfig());
                final Method methodAuthenticate = aegis.getAuthorizer().getAuthenticate();
                final Object reference = methodAuthenticate.invoke(aegis.getProxy(), vertx, config);
                return null == reference ? null : (AuthenticationHandler) reference;
            });
        } else {
            return null;
        }
    }

    /*
     *  Extract AuthorizationHandler
     */
    @Override
    public AuthorizationHandler access(final Vertx vertx, final Aegis aegis) {
        Objects.requireNonNull(aegis);
        if (aegis.okForAccess()) {
            return Fn.getJvm(() -> {
                // Config Extracting
                final JsonObject config = Ut.sureJObject(aegis.getConfig());
                final Method methodAuthenticate = aegis.getAuthorizer().getAuthorize();
                final Object reference = methodAuthenticate.invoke(aegis.getProxy(), vertx, config);
                return null == reference ? null : (AuthorizationHandler) reference;
            });
        } else {
            return null;
        }
    }
}
