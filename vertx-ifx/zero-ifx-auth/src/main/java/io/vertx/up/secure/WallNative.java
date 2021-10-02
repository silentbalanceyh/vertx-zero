package io.vertx.up.secure;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.AegisItem;
import io.vertx.up.eon.em.AuthWall;
import io.vertx.up.fn.Fn;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WallNative implements LeeNative {
    private static final ConcurrentMap<String, Lee> LEE_POOL = new ConcurrentHashMap<>();

    private static final ConcurrentMap<AuthWall, Supplier<Lee>> LEE_SUPPLIER = new ConcurrentHashMap<>() {
        {
            this.put(AuthWall.BASIC, BasicLee::new);
            this.put(AuthWall.DIGEST, DigestLee::new);
            this.put(AuthWall.JWT, JwtLee::new);
            this.put(AuthWall.OAUTH2, OAuth2Lee::new);
            this.put(AuthWall.REDIRECT, RedirectLee::new);
            this.put(AuthWall.WEB, WebLee::new);
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
    public String encode(final JsonObject data, final AegisItem config) {
        final Lee reference = this.component(config.wall());
        return reference.encode(data, config);
    }

    @Override
    public JsonObject decode(final String token, final AegisItem config) {
        final Lee reference = this.component(config.wall());
        return reference.decode(token, config);
    }

    private Lee component(final AuthWall wall) {
        final Supplier<Lee> supplier = LEE_SUPPLIER.getOrDefault(wall, null);
        Objects.requireNonNull(supplier);
        return Fn.poolThread(LEE_POOL, supplier, wall.key());
    }
}
