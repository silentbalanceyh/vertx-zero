package io.vertx.up.secure;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.impl.jose.JWT;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.AegisItem;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class LeeJwt extends AbstractLee {
    private static final ConcurrentMap<String, JWTAuth> POOL_PROVIDER = new ConcurrentHashMap<>();

    @Override
    public AuthenticationHandler authenticate(final Vertx vertx, final Aegis config) {
        final JWTAuth provider = this.provider(vertx, config.item());
        // Jwt Handler Generated
        final String realm = this.option(config, "realm");
        final JWTAuthHandler standard;
        if (Ut.isNil(realm)) {
            standard = JWTAuthHandler.create(provider);
        } else {
            standard = JWTAuthHandler.create(provider, realm);
        }
        return this.wrapHandler(standard, config);
    }

    private JWTAuth provider(final Vertx vertx, final AegisItem item) {
        // Options
        final JWTAuthOptions options = new JWTAuthOptions(item.options());
        final String key = item.wall().name() + options.hashCode();
        return Fn.poolThread(POOL_PROVIDER, () -> JWTAuth.create(vertx, options), key);
    }

    @Override
    public String encode(final JsonObject data, final AegisItem config) {
        final JWTAuth provider = this.provider(Ux.nativeVertx(), config);
        return provider.generateToken(data);
    }

    @Override
    public JsonObject decode(final String token, final AegisItem config) {
        final JWTAuth provider = this.provider(Ux.nativeVertx(), config);
        final JWT jwt = Ut.field(provider, "jwt");
        return Objects.isNull(jwt) ? new JsonObject() : jwt.decode(token);
    }
}
