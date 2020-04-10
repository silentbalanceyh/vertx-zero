package io.vertx.up.secure.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.impl.AuthProviderInternal;
import io.vertx.ext.web.RoutingContext;

import java.util.Base64;

public class BasicPhylum extends AuthorizationAuthPhylum {

    public BasicPhylum(final AuthProvider authProvider, final String realm) {
        super(verifyProvider(authProvider), realm, Type.BASIC);
    }

    /**
     * This is a verification step, it can abort the instantiation by
     * throwing a RuntimeException
     */
    private static AuthProvider verifyProvider(final AuthProvider provider) {
        if (provider instanceof AuthProviderInternal) {
            ((AuthProviderInternal) provider).verifyIsUsingPassword();
        }
        return provider;
    }

    @Override
    public void parseCredentials(final RoutingContext context, final Handler<AsyncResult<JsonObject>> handler) {

        this.parseAuthorization(context, false, parseAuthorization -> {
            if (parseAuthorization.failed()) {
                handler.handle(Future.failedFuture(parseAuthorization.cause()));
                return;
            }

            final String suser;
            final String spass;

            try {
                // decode the payload
                final String decoded = new String(Base64.getDecoder().decode(parseAuthorization.result()));

                final int colonIdx = decoded.indexOf(":");
                if (colonIdx != -1) {
                    suser = decoded.substring(0, colonIdx);
                    spass = decoded.substring(colonIdx + 1);
                } else {
                    suser = decoded;
                    spass = null;
                }
            } catch (final RuntimeException e) {
                // IllegalArgumentException includes PatternSyntaxException
                context.fail(e);
                return;
            }
            /*
             * {
             *     "username":"Basic user name",
             *     "password":"Basic password"
             * }
             */
            handler.handle(Future.succeededFuture(new JsonObject()
                    .put("username", suser)
                    .put("password", spass)));
        });
    }

    @Override
    protected String authenticateHeader(final RoutingContext context) {
        return "Basic realm=\"" + this.realm + "\"";
    }

}
