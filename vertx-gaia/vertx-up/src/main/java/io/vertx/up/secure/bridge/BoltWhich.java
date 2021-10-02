package io.vertx.up.secure.bridge;

import io.vertx.core.Vertx;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.eon.em.AuthWall;
import io.vertx.up.secure.Lee;
import io.vertx.up.secure.LeeExtension;
import io.vertx.up.secure.LeeNative;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BoltWhich implements Bolt {
    static ConcurrentMap<String, Bolt> POOL_BOLT = new ConcurrentHashMap<>();

    @Override
    public AuthenticationHandler authenticate(final Vertx vertx, final Aegis config) {
        if (config.noAuthentication()) {
            return null;
        }
        final Aegis verified = this.verifyConfig(config);
        return null;
    }

    @Override
    public AuthorizationHandler authorization(final Vertx vertx, final Aegis config) {
        if (config.noAuthorization()) {
            return null;
        }
        return null;
    }

    private Aegis verifyConfig(final Aegis config) {

        return config;
    }

    private Lee reference(final Aegis config) {
        final AuthWall wall = config.getType();
        if (AuthWall.EXTENSION == wall) {
            return Ut.service(LeeExtension.class);
        } else {
            return Ut.service(LeeNative.class);
        }
    }
}
