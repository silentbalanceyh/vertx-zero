package io.vertx.up.secure;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.ext.web.handler.BasicAuthHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.AegisItem;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class BasicLee extends AbstractLee {

    @Override
    public AuthenticationHandler authenticate(final Vertx vertx, final Aegis config) {
        final AuthenticationProvider provider = this.providerAuthenticate(config);
        final String realm = this.option(config, "realm");
        if (Ut.isNil(realm)) {
            return BasicAuthHandler.create(provider);
        } else {
            return BasicAuthHandler.create(provider, realm);
        }
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
