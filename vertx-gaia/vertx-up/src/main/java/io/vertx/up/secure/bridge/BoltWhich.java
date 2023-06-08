package io.vertx.up.secure.bridge;

import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.commune.secure.Aegis;
import io.vertx.up.commune.secure.AegisItem;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.exception.boot.WallItemSizeException;
import io.vertx.up.exception.boot.WallProviderConflictException;
import io.vertx.up.fn.Fn;
import io.vertx.up.secure.Lee;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class BoltWhich implements Bolt {
    private static final Annal LOGGER = Annal.get(BoltWhich.class);
    // LOGGER Control
    private static final AtomicBoolean[] LOG_LEE = new AtomicBoolean[]{
        new AtomicBoolean(Boolean.TRUE),
        new AtomicBoolean(Boolean.TRUE),
        new AtomicBoolean(Boolean.TRUE)
    };

    static Cc<String, Bolt> CC_BOLT = Cc.openThread();
    static Cc<String, Lee> CC_LEE = Cc.openThread();

    @Override
    public AuthenticationHandler authenticate(final Vertx vertx, final Aegis config) {
        Objects.requireNonNull(config);
        if (config.noAuthentication()) {
            // Log
            if (LOG_LEE[0].getAndSet(Boolean.FALSE)) {
                LOGGER.warn(INFO.AUTH_401_METHOD, config);
            }
            return null;
        }
        final Aegis verified = this.verifyAuthenticate(config);
        final Lee lee = Bolt.reference(config.getType());
        if (Objects.isNull(lee)) {
            // Log
            if (LOG_LEE[1].getAndSet(Boolean.FALSE)) {
                LOGGER.warn(INFO.AUTH_401_SERVICE, config.getType());
            }
            return null;
        }
        final AuthenticationHandler handler = lee.authenticate(vertx, verified);
        if (Objects.isNull(handler)) {
            // Log
            if (LOG_LEE[2].getAndSet(Boolean.FALSE)) {
                LOGGER.warn(INFO.AUTH_401_HANDLER, config.getType());
            }
        }
        return handler;
    }

    @Override
    public AuthorizationHandler authorization(final Vertx vertx, final Aegis config) {
        Objects.requireNonNull(config);
        if (config.noAuthorization()) {
            return null;
        }
        final Lee lee = Bolt.reference(config.getType());
        if (Objects.isNull(lee)) {
            return null;
        }
        return lee.authorization(vertx, config);
    }

    @Override
    public AuthenticationProvider authenticateProvider(final Vertx vertx, final Aegis config) {
        Objects.requireNonNull(config);
        if (config.noAuthentication()) {
            return null;
        }
        final Lee lee = Bolt.reference(config.getType());
        if (Objects.isNull(lee)) {
            return null;
        }
        return lee.provider(vertx, config);
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
        if (EmSecure.AuthWall.EXTENSION != config.getType()) {
            /*
             * The size should be 1 ( For non-extension )
             */
            final AegisItem item = config.item();
            Fn.outBoot(Objects.isNull(item), WallItemSizeException.class,
                this.getClass(), config.getType(), 1);
        }
        final Set<Class<?>> provider = config.providers();
        /*
         * Must be valid type of provider
         */
        provider.forEach(item -> Fn.outBoot(!AuthenticationProvider.class.isAssignableFrom(item),
            WallProviderConflictException.class,
            this.getClass(), item));
        return config;
    }
}
