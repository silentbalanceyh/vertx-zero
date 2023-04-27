package io.vertx.up.secure;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.impl.jose.JWT;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.AegisItem;
import io.vertx.up.secure.authenticate.AdapterProvider;
import io.horizon.uca.cache.Cc;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class LeeJwt extends AbstractLee {
    private static final Cc<String, JWTAuth> CC_PROVIDER = Cc.openThread();

    @Override
    public AuthenticationHandler authenticate(final Vertx vertx, final Aegis config) {
        final JWTAuth provider = this.providerInternal(vertx, config);
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

    @Override
    public AuthenticationProvider provider(final Vertx vertx, final Aegis config) {
        final JWTAuth standard = this.providerInternal(vertx, config);
        final AdapterProvider extension = AdapterProvider.extension(standard);
        return extension.provider(config);
    }

    @Override
    @SuppressWarnings("unchecked")
    public JWTAuth providerInternal(final Vertx vertx, final Aegis config) {
        // Options
        final AegisItem item = config.item();
        return this.provider(vertx, item);
    }

    private JWTAuth provider(final Vertx vertx, final AegisItem item) {
        final JWTAuthOptions options = new JWTAuthOptions(item.options());
        final String key = item.wall().name() + options.hashCode();
        return CC_PROVIDER.pick(() -> JWTAuth.create(vertx, options), key);
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
