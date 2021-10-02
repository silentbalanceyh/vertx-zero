package io.vertx.up.secure;

import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.AegisItem;
import io.vertx.up.eon.em.AuthWall;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.secure.error.ProviderMissingException;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
abstract class AbstractLee implements LeeNative {

    private static final AtomicBoolean LOG_401 = new AtomicBoolean(Boolean.TRUE);

    protected AuthenticationProvider providerAuthenticate(final Aegis aegis) {
        final AegisItem item = aegis.item();
        final Class<?> providerCls = item.getProviderAuthenticate();

        final AuthWall wall = aegis.getType();
        if (AuthWall.BASIC == wall || AuthWall.REDIRECT == wall) {
            Fn.outUp(Objects.isNull(providerCls), this.logger(), ProviderMissingException.class, this.getClass(), wall);
        }
        final AuthenticationProvider provider = (AuthenticationProvider) Ut.invokeStatic(providerCls, "create", aegis);
        if (Objects.isNull(provider)) {
            if (LOG_401.getAndSet(Boolean.FALSE)) {
                this.logger().error("[ Auth ] 401 provider created failure! type = {0}", wall);
            }
        }
        return provider;
    }

    protected AuthorizationProvider providerAuthorization(final Aegis aegis) {
        final AegisItem item = aegis.item();
        final Class<?> providerCls = item.getProviderAuthorization();
        return Objects.isNull(providerCls) ? null : (AuthorizationProvider) Ut.invokeStatic(providerCls, "create", aegis);
    }

    protected <T> T option(final Aegis aegis, final String key) {
        final AegisItem item = aegis.item();
        return (T) item.options().getValue(key, null);
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
