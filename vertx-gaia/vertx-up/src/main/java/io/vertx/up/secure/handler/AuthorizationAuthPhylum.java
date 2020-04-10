package io.vertx.up.secure.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.RoutingContext;

abstract class AuthorizationAuthPhylum extends AuthPhylum {
    protected final AuthorizationAuthPhylum.Type type;

    AuthorizationAuthPhylum(final AuthProvider authProvider, final AuthorizationAuthPhylum.Type type) {
        super(authProvider);
        this.type = type;
    }

    AuthorizationAuthPhylum(final AuthProvider authProvider, final String realm, final AuthorizationAuthPhylum.Type type) {
        super(authProvider, realm);
        this.type = type;
    }

    protected final void parseAuthorization(final RoutingContext ctx, final boolean optional, final Handler<AsyncResult<String>> handler) {

        final HttpServerRequest request = ctx.request();
        // Get http header: Authorization value
        final String authorization = request.headers().get(HttpHeaders.AUTHORIZATION);

        if (authorization == null) {
            /*
             * The modification for default implementation in vert.x,
             * Here means that the system could not get Authorization http header,
             * default to 401 Error.
             */
            handler.handle(Future.failedFuture(this.UNAUTHORIZED));
            return;
        }

        try {
            /*
             * The header Authorization value must be `TYPE VALUE` format
             * it means that you must set this value correct
             */
            final int idx = authorization.indexOf(' ');

            if (idx <= 0) {
                /*
                 * Wrong format of Authorization, it means that format is wrong
                 * The system will predicate to check the format first.
                 */
                handler.handle(Future.failedFuture(this.BAD_REQUEST));
                return;
            }

            if (!this.type.is(authorization.substring(0, idx))) {
                /*
                 * The type must be one of following:
                 * 1. Implementation widely
                 * Basic
                 * Digest
                 * Bearer
                 * 2. No known implementation ( may be defined by developer )
                 * HOBA
                 * Mutual
                 * Negotiate
                 * OAuth
                 * SCRAM-SHA-1
                 * SCRAM-SHA-256
                 */
                handler.handle(Future.failedFuture(this.UNAUTHORIZED));
                return;
            }
            /*
             * Trim will remove the whitespace between TYPE and VALUE
             * It's not strict to let developer must set `TYPE VALUE` format, the system should do following tasks:
             * 1. Verify whether the type is correct.
             * 2. Extract the value from Authorization.
             */
            handler.handle(Future.succeededFuture(authorization.substring(idx + 1).trim()));
        } catch (final RuntimeException e) {
            // TODO: For Debug
            e.printStackTrace();
            // Runtime error for exception cache
            handler.handle(Future.failedFuture(e));
        }
    }

    // this should match the IANA registry: https://www.iana.org/assignments/http-authschemes/http-authschemes.xhtml
    enum Type {
        BASIC("Basic"),
        DIGEST("Digest"),
        BEARER("Bearer"),
        // these have no known implementation
        HOBA("HOBA"),
        MUTUAL("Mutual"),
        NEGOTIATE("Negotiate"),
        OAUTH("OAuth"),
        SCRAM_SHA_1("SCRAM-SHA-1"),
        SCRAM_SHA_256("SCRAM-SHA-256");

        private final String label;

        Type(final String label) {
            this.label = label;
        }

        public boolean is(final String other) {
            return this.label.equalsIgnoreCase(other);
        }
    }
}
