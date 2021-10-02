package io.vertx.up.secure.bridge;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.tp.error.WallItemSizeException;
import io.vertx.tp.error.WallProviderConflictException;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.eon.em.AuthWall;
import io.vertx.up.fn.Fn;
import io.vertx.up.secure.Lee;
import io.vertx.up.secure.LeeExtension;
import io.vertx.up.secure.LeeNative;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
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
        final Aegis verified = this.verifyAuthenticate(config);
        final Lee lee = this.reference(config);
        if (Objects.isNull(lee)) {
            return null;
        }
        return lee.authenticate(vertx, verified);
    }

    @Override
    public AuthorizationHandler authorization(final Vertx vertx, final Aegis config) {
        if (config.noAuthorization()) {
            return null;
        }
        final Lee lee = this.reference(config);
        if (Objects.isNull(lee)) {
            return null;
        }
        return lee.authorization(vertx, config);
    }

    /*
     * Here the validation rules
     * 1. The size of provider should be matched
     * - Extension could be > 1
     * - Others must be = 1
     * 2. All the following must be match
     * - JWT, WEB, OAUTH2, DIGEST
     * They are fixed provider of authenticate
     */
    private Aegis verifyAuthenticate(final Aegis config) {
        final int itemSize = config.item().size();
        if (AuthWall.EXTENSION != config.getType()) {
            /*
             * The size should be 1 ( For non-extension )
             */
            Fn.outUp(1 != itemSize, WallItemSizeException.class,
                this.getClass(), config.getType(), itemSize);
        }
        final Set<Class<?>> provider = config.providers();
        /*
         * Must be valid type of provider
         */
        provider.forEach(item -> Fn.outUp(!AuthenticationProvider.class.isAssignableFrom(item),
            WallProviderConflictException.class,
            this.getClass(), item));
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
