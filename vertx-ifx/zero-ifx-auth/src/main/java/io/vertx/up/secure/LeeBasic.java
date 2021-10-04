package io.vertx.up.secure;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.BasicAuthHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.AegisItem;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.secure.authenticate.ProviderAdapter;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class LeeBasic extends AbstractLee {
    @Override
    public AuthenticationHandler authenticate(final Vertx vertx, final Aegis config) {
        /*
         * Here provider could build ChainAuth instead of handler chain
         * The provider type is: io.vertx.ext.auth.ChainAuth
         */
        final ProviderAdapter adapter = ProviderAdapter.common();
        final AuthenticationProvider provider = adapter.provider(config);
        // Basic Handler Generated
        final String realm = this.option(config, "realm");
        if (Ut.isNil(realm)) {
            return BasicAuthHandler.create(provider);
        } else {
            return BasicAuthHandler.create(provider, realm);
        }
    }

    /*
     * {
     *      "username": "",
     *      "password": xxx
     * }
     *
     * username must not be null, but password could be null.
     */
    @Override
    public String encode(final JsonObject data, final AegisItem config) {
        final String username = data.getString(KName.USERNAME, null);
        if (Ut.isNil(username)) {
            return null;
        }
        final String password = data.getString(KName.PASSWORD, Strings.EMPTY);
        return Ut.encryptBase64(username, password);
    }

    @Override
    public JsonObject decode(final String token, final AegisItem config) {
        final String decoded = Ut.decryptBase64(token);
        final int colonIdx = decoded.indexOf(":");
        final JsonObject data = new JsonObject();
        if (colonIdx != -1) {
            data.put(KName.USERNAME, decoded.substring(0, colonIdx));
            data.put(KName.PASSWORD, decoded.substring(colonIdx + 1));
        } else {
            data.put(KName.USERNAME, decoded);
        }
        return data;
    }
}
