package io.vertx.up.atom.secure;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;

/**
 * Link the module of following
 * 1. vertx-auth-jwt
 * 2. vertx-auth-oauth2
 * 3. vertx-auth-webauthn
 * For extension security configuration, this interface is required to splitting
 *
 * However, this interface will be called by `Ux.Jwt` class internal for token processing
 * 1. Generate new Jwt token
 * 2. Extract data from Jwt token
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Lee {
    /*
     * 1. Authenticate Handler
     */
    AuthenticationHandler authenticate(Vertx vertx, AegisItem config);

    /*
     * 2. Authorization Handler
     */
    AuthorizationHandler authorization(Vertx vertx, AegisItem config);

    /*
     * 3. Token operation
     */
    String encode(JsonObject data, AegisItem config);

    JsonObject decode(String token, AegisItem config);
}
