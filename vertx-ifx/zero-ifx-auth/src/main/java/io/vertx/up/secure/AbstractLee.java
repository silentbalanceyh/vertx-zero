package io.vertx.up.secure;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.ext.web.handler.ChainAuthHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.AegisItem;
import io.vertx.up.eon.em.AuthWall;
import io.vertx.up.log.Annal;
import io.vertx.up.secure.authenticate.AuthenticateBuiltInHandler;
import io.vertx.up.secure.authorization.AuthorizationBuiltInHandler;
import io.vertx.up.secure.authorization.AuthorizationBuiltInProvider;
import io.vertx.up.secure.authorization.AuthorizationExtensionHandler;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
abstract class AbstractLee implements LeeBuiltIn {

    // --------------------------- Interface Method

    @Override
    public AuthorizationHandler authorization(final Vertx vertx, final Aegis config) {
        final Class<?> handlerCls = config.getHandler();
        if (Objects.isNull(handlerCls)) {
            // Default profile is no access ( 403 )
            final AuthorizationHandler handler = AuthorizationBuiltInHandler.create(config);
            final AuthorizationProvider provider = AuthorizationBuiltInProvider.provider(config);
            handler.addAuthorizationProvider(provider);
            {
                /*
                 * Check whether user defined provider, here are defined provider
                 * for current 403 workflow instead of standard workflow here
                 */
                final AegisItem item = config.item();
                final Class<?> providerCls = item.getProviderAuthenticate();
                if (Objects.isNull(providerCls)) {
                    return null;
                }
                final AuthWall wall = config.getType();
                final AuthorizationProvider defined = Ut.invokeStatic(providerCls, "provider", config);
                if (Objects.nonNull(defined)) {
                    handler.addAuthorizationProvider(defined);
                }
            }
            return handler;
        } else {
            // The class must contain constructor with `(Vertx)`
            return ((AuthorizationExtensionHandler) Ut.instance(handlerCls, vertx)).configure(config);
        }
    }

    protected AuthenticationHandler wrapHandler(final AuthenticationHandler standard, final Aegis aegis) {
        final ChainAuthHandler handler = ChainAuthHandler.all();
        handler.add(standard);
        handler.add(AuthenticateBuiltInHandler.create(aegis));
        return handler;
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
