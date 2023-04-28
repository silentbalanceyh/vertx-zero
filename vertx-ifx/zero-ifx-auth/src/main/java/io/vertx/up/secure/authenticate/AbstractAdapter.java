package io.vertx.up.secure.authenticate;

import io.horizon.eon.em.secure.AuthWall;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.AegisItem;
import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractAdapter implements AdapterProvider {
    private static final AtomicBoolean LOG_401 = new AtomicBoolean(Boolean.TRUE);

    protected AuthenticationProvider provider401Internal(final Aegis aegis) {
        final AegisItem item = aegis.item();
        final Class<?> providerCls = item.getProviderAuthenticate();
        if (Objects.isNull(providerCls)) {
            return null;
        }
        final AuthWall wall = aegis.getType();
        final AuthenticationProvider provider = Ut.invokeStatic(providerCls, "provider", aegis);
        if (Objects.isNull(provider)) {
            if (LOG_401.getAndSet(Boolean.FALSE)) {
                this.logger().error("[ Auth ] 401 provider created failure! type = {0}", wall);
            }
        }
        return provider;
    }

    private Annal logger() {
        return Annal.get(this.getClass());
    }
}
