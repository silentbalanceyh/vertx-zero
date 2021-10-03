package io.vertx.up.secure;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.ChainAuth;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.AegisItem;
import io.vertx.up.eon.em.AuthWall;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.secure.authenticate.AuthenticateBuiltInProvider;
import io.vertx.up.secure.authorization.AuthorizationBuiltInHandler;
import io.vertx.up.secure.authorization.AuthorizationBuiltInProviderImpl;
import io.vertx.up.secure.authorization.AuthorizationExtensionHandler;
import io.vertx.up.secure.cv.LeePool;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
abstract class AbstractLee implements LeeBuiltIn {

    private static final AtomicBoolean LOG_401 = new AtomicBoolean(Boolean.TRUE);

    // --------------------------- 401
    /*
     * Configured: 401 provider
     */
    private AuthenticationProvider provider401Internal(final Aegis aegis) {
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

    protected AuthenticationProvider provider401(final Aegis config) {
        // Chain Provider
        final ChainAuth chain = ChainAuth.all();
        final AuthenticationProvider provider = this.provider401Internal(config);
        if (Objects.nonNull(provider)) {
            chain.add(provider);
        }
        // 2. Wall Provider ( Based on Annotation )
        final AuthenticationProvider wallProvider = AuthenticateBuiltInProvider.provider(config);
        chain.add(wallProvider);
        return chain;
    }

    // --------------------------- 403
    /*
     * Configured: 403 provider
     */
    private AuthorizationProvider provider403Internal(final Aegis aegis) {
        final AegisItem item = aegis.item();
        final Class<?> providerCls = item.getProviderAuthenticate();
        if (Objects.isNull(providerCls)) {
            return null;
        }
        final AuthWall wall = aegis.getType();
        return (AuthorizationProvider) Ut.invokeStatic(providerCls, "provider", aegis);
    }

    // --------------------------- Interface Method
    @Override
    public AuthorizationHandler authorization(final Vertx vertx, final Aegis config) {
        final Class<?> handlerCls = config.getHandler();
        if (Objects.isNull(handlerCls)) {
            // Default profile is no access ( 403 )
            final AuthorizationHandler handler = AuthorizationBuiltInHandler.create();
            final AuthorizationProvider provider = AuthorizationBuiltInProviderImpl.provider(config);
            /*
             * Check whether user defined provider
             */
            handler.addAuthorizationProvider(provider);
            final AuthorizationProvider defined = this.provider403Internal(config);
            if (Objects.nonNull(defined)) {
                handler.addAuthorizationProvider(defined);
            }
            return handler;
        } else {
            // The class must contain constructor with `(Vertx, JsonObject)`
            final AuthorizationExtensionHandler handler = (AuthorizationExtensionHandler) Fn.poolThread(LeePool.POOL_HANDLER,
                () -> Ut.instance(handlerCls, vertx), handlerCls.getName());
            handler.configure(config);
            return handler;
        }
    }

    // --------------------------- Sub class only

    protected <T> T option(final Aegis aegis, final String key) {
        final AegisItem item = aegis.item();
        return (T) item.options().getValue(key, null);
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
