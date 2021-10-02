package io.vertx.up.secure;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.AegisItem;
import io.vertx.up.eon.em.AuthWall;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.secure.error.ProviderMissingException;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class BasicLee implements LeeNative {
    private static final Annal LOGGER = Annal.get(BasicLee.class);

    @Override
    public AuthenticationHandler authenticate(final Vertx vertx, final Aegis config) {
        final AegisItem item = config.item(AuthWall.BASIC);
        final Class<?> providerCls = item.getProviderAuthenticate();
        Fn.outUp(Objects.isNull(providerCls), LOGGER, ProviderMissingException.class, this.getClass(), item.wall());
        return null;
    }

    @Override
    public AuthorizationHandler authorization(final Vertx vertx, final Aegis config) {
        return null;
    }

    @Override
    public String encode(final JsonObject data, final AegisItem config) {
        return null;
    }

    @Override
    public JsonObject decode(final String token, final AegisItem config) {
        return null;
    }
}
