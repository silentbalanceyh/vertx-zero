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

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
abstract class AbstractLee implements LeeNative {

    protected AuthenticationProvider providerAuthenticate(final Aegis aegis) {
        final AegisItem item = aegis.item();
        final Class<?> providerCls = item.getProviderAuthenticate();

        final AuthWall wall = aegis.getType();
        if (AuthWall.BASIC == wall || AuthWall.REDIRECT == wall) {
            Fn.outUp(Objects.isNull(providerCls), this.logger(), ProviderMissingException.class, this.getClass(), wall);
        }
        return (AuthenticationProvider) Ut.invoke(providerCls, "create", aegis);
    }

    protected AuthorizationProvider providerAuthorization(final Aegis aegis) {
        final AegisItem item = aegis.item();
        final Class<?> providerCls = item.getProviderAuthorization();
        return Objects.isNull(providerCls) ? null : (AuthorizationProvider) Ut.invoke(providerCls, "create", aegis);
    }

    protected <T> T option(final Aegis aegis, final String key) {
        final AegisItem item = aegis.item();
        return (T) item.options().getValue(key, null);
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
