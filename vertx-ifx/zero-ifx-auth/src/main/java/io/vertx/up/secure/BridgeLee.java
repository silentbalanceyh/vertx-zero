package io.vertx.up.secure;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.commune.secure.Aegis;
import io.vertx.up.commune.secure.AegisItem;
import io.vertx.up.eon.em.EmSecure;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BridgeLee implements LeeBuiltIn {
    private static final Cc<String, Lee> CC_LEE = Cc.openThread();

    private static final ConcurrentMap<EmSecure.AuthWall, Supplier<Lee>> LEE_SUPPLIER = new ConcurrentHashMap<>() {
        {
            this.put(EmSecure.AuthWall.BASIC, LeeBasic::new);
            this.put(EmSecure.AuthWall.DIGEST, LeeDigest::new);
            this.put(EmSecure.AuthWall.JWT, LeeJwt::new);
            this.put(EmSecure.AuthWall.OAUTH2, LeeOAuth2::new);
        }
    };

    @Override
    public AuthenticationHandler authenticate(final Vertx vertx, final Aegis config) {
        final Lee reference = this.component(config.getType());
        return reference.authenticate(vertx, config);
    }

    @Override
    public AuthorizationHandler authorization(final Vertx vertx, final Aegis config) {
        final Lee reference = this.component(config.getType());
        return reference.authorization(vertx, config);
    }

    @Override
    public AuthenticationProvider provider(final Vertx vertx, final Aegis config) {
        final Lee reference = this.component(config.getType());
        return reference.provider(vertx, config);
    }

    @Override
    public String encode(final JsonObject data, final AegisItem config) {
        final Lee reference = this.component(config.wall());
        return reference.encode(data, config);
    }

    @Override
    public JsonObject decode(final String token, final AegisItem config) {
        final Lee reference = this.component(config.wall());
        return reference.decode(token, config);
    }

    private Lee component(final EmSecure.AuthWall wall) {
        final Supplier<Lee> supplier = LEE_SUPPLIER.getOrDefault(wall, null);
        Objects.requireNonNull(supplier);
        return CC_LEE.pick(supplier, wall.key()); // Fn.po?lThread(LEE_POOL, supplier, wall.key());
    }
}
