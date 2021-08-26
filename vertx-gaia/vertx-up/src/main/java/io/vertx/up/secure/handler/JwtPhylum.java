package io.vertx.up.secure.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.secure.provider.JwtAuth;

import java.util.List;

public class JwtPhylum extends AuthorizationAuthPhylum implements JwtOstium {
    private final String skip;
    private final JsonObject options;

    public JwtPhylum(final JwtAuth authProvider, final String skip) {
        super(authProvider, AuthorizationAuthPhylum.Type.BEARER);
        this.skip = skip;
        this.options = new JsonObject();
    }

    @Override
    public JwtOstium setAudience(final List<String> audience) {
        this.options.put("audience", new JsonArray(audience));
        return this;
    }

    @Override
    public JwtOstium setIssuer(final String issuer) {
        this.options.put("issuer", issuer);
        return this;
    }

    @Override
    public JwtOstium setIgnoreExpiration(final boolean ignoreExpiration) {
        this.options.put("ignoreExpiration", ignoreExpiration);
        return this;
    }

    @Override
    public void parseCredentials(final RoutingContext context, final Handler<AsyncResult<JsonObject>> handler) {
        if (this.skip != null && context.normalisedPath().startsWith(this.skip)) {
            context.next();
        } else {
            this.parseAuthorization(context, false, (parseAuthorization) -> {
                if (parseAuthorization.failed()) {
                    handler.handle(Future.failedFuture(parseAuthorization.cause()));
                } else {
                    /*
                     * {
                     *     "jwt": "token value",
                     *     "options":{
                     *     }
                     * }
                     */
                    handler.handle(Future.succeededFuture((new JsonObject())
                        .put("jwt", parseAuthorization.result())
                        .put("options", this.options)));
                }
            });
        }
    }

    @Override
    protected String authenticateHeader(final RoutingContext context) {
        return "Bearer";
    }
}
