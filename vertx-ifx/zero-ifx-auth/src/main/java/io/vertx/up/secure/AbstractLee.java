package io.vertx.up.secure;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.ChainAuth;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.AegisItem;
import io.vertx.up.eon.em.AuthWall;
import io.vertx.up.log.Annal;
import io.vertx.up.secure.authenticate.WallAuth;
import io.vertx.up.secure.authorization.PermissionHandler;
import io.vertx.up.secure.authorization.PermissionZeroProvider;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
abstract class AbstractLee implements LeeNative {

    private static final AtomicBoolean LOG_401 = new AtomicBoolean(Boolean.TRUE);

    /*
     * User defined: 401 provider
     */
    private AuthenticationProvider providerAuthenticate(final Aegis aegis) {
        final AegisItem item = aegis.item();
        final Class<?> providerCls = item.getProviderAuthenticate();
        if (Objects.isNull(providerCls)) {
            return null;
        }
        final AuthWall wall = aegis.getType();
        final AuthenticationProvider provider = (AuthenticationProvider) Ut.invokeStatic(providerCls, "provider", aegis);
        if (Objects.isNull(provider)) {
            if (LOG_401.getAndSet(Boolean.FALSE)) {
                this.logger().error("[ Auth ] 401 provider created failure! type = {0}", wall);
            }
        }
        return provider;
    }

    protected AuthenticationProvider providerAuthenticate(final Aegis config, final AuthenticationProvider nativeProvider) {
        // Chain
        final ChainAuth chain = ChainAuth.all();
        if (Objects.nonNull(nativeProvider)) {
            // Native
            chain.add(nativeProvider);
        }

        final AuthenticationProvider provider = this.providerAuthenticate(config);
        if (Objects.nonNull(provider)) {
            // User Defined
            chain.add(provider);
        }
        // Wall
        final AuthenticationProvider wall = WallAuth.provider(config);
        chain.add(wall);
        return chain;
    }

    private AuthorizationProvider providerAuthorization(final Aegis aegis) {
        final AegisItem item = aegis.item();
        final Class<?> providerCls = item.getProviderAuthenticate();
        if (Objects.isNull(providerCls)) {
            return null;
        }
        final AuthWall wall = aegis.getType();
        return (AuthorizationProvider) Ut.invokeStatic(providerCls, "provider", aegis);
    }

    @Override
    public AuthorizationHandler authorization(final Vertx vertx, final Aegis config) {
        // Default profile is no access ( 403 )
        final AuthorizationHandler handler = PermissionHandler.create();
        final AuthorizationProvider provider = PermissionZeroProvider.provider(config);
        /*
         * Check whether user defined provider
         */
        handler.addAuthorizationProvider(provider);
        final AuthorizationProvider defined = this.providerAuthorization(config);
        if (Objects.nonNull(defined)) {
            handler.addAuthorizationProvider(defined);
        }
        return handler;
    }

    protected <T> T option(final Aegis aegis, final String key) {
        final AegisItem item = aegis.item();
        return (T) item.options().getValue(key, null);
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
