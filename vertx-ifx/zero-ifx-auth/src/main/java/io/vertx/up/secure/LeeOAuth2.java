package io.vertx.up.secure;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2Options;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.OAuth2AuthHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.AegisItem;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class LeeOAuth2 extends AbstractLee {
    private static final ConcurrentMap<String, OAuth2Auth> POOL_PROVIDER = new ConcurrentHashMap<>();

    @Override
    public AuthenticationHandler authenticate(final Vertx vertx, final Aegis config) {
        // Options
        final OAuth2Auth provider = this.provider(vertx, config.item());
        final String callback = this.option(config, "callback");
        final OAuth2AuthHandler standard;
        if (Ut.isNil(callback)) {
            standard = OAuth2AuthHandler.create(vertx, provider);
        } else {
            standard = OAuth2AuthHandler.create(vertx, provider, callback);
        }
        return this.wrapHandler(standard, config);
    }

    private OAuth2Auth provider(final Vertx vertx, final AegisItem item) {
        // Options
        final OAuth2Options options = new OAuth2Options(item.options());
        final String key = item.wall().name() + options.hashCode();
        return Fn.poolThread(POOL_PROVIDER, () -> OAuth2Auth.create(vertx, options), key);
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
