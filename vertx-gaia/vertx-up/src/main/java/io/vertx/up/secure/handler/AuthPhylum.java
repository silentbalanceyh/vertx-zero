package io.vertx.up.secure.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.*;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._400BadRequestException;
import io.vertx.up.exception.web._401UnauthorizedException;
import io.vertx.up.exception.web._403ForbiddenException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.log.Annal;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("all")
public abstract class AuthPhylum implements AuthHandler {

    static final String AUTH_PROVIDER_CONTEXT_KEY = "io.vertx.ext.web.handler.AuthHandler.provider";
    protected final String realm;
    protected final Set<String> authorities = new HashSet<>();
    final WebException FORBIDDEN = new _403ForbiddenException(this.getClass());
    final WebException UNAUTHORIZED = new _401UnauthorizedException(this.getClass());
    final WebException BAD_REQUEST = new _400BadRequestException(this.getClass());
    // Provider binding
    protected transient AuthProvider authProvider;

    public AuthPhylum(final AuthProvider authProvider) {
        this(authProvider, "");
    }

    public AuthPhylum(final AuthProvider authProvider, final String realm) {
        this.authProvider = authProvider;
        this.realm = realm;
    }

    @Override
    public AuthHandler addAuthority(final String authority) {
        this.authorities.add(authority);
        return this;
    }

    @Override
    public AuthHandler addAuthorities(final Set<String> authorities) {
        this.authorities.addAll(authorities);
        return this;
    }

    protected String authenticateHeader(final RoutingContext context) {
        return null;
    }

    @Override
    public void authorize(final User user, final Handler<AsyncResult<Void>> handler) {
        final int requiredcount = this.authorities.size();
        if (requiredcount > 0) {
            if (user == null) {
                handler.handle(Future.failedFuture(this.FORBIDDEN));
                return;
            }

            final AtomicInteger count = new AtomicInteger();
            final AtomicBoolean sentFailure = new AtomicBoolean();

            final Handler<AsyncResult<Boolean>> authHandler = res -> {
                if (res.succeeded()) {
                    if (res.result()) {
                        if (count.incrementAndGet() == requiredcount) {
                            // Has all required authorities
                            handler.handle(Future.succeededFuture());
                        }
                    } else {
                        if (sentFailure.compareAndSet(false, true)) {
                            handler.handle(Future.failedFuture(this.FORBIDDEN));
                        }
                    }
                } else {
                    handler.handle(Future.failedFuture(res.cause()));
                }
            };
            for (final String authority : this.authorities) {
                if (!sentFailure.get()) {
                    user.isAuthorized(authority, authHandler);
                }
            }
        } else {
            // No auth required
            handler.handle(Future.succeededFuture());
        }
    }

    @Override
    public void handle(final RoutingContext ctx) {
        /*
         * Because AuthPhylum component is Handler of vert.x, here it the first step
         * Call handlePreflight to verify http specification
         * Please refer: https://www.w3.org/TR/cors/#cross-origin-request-with-preflight-0
         * Cross domain
         */
        if (this.handlePreflight(ctx)) {
            return;
        }

        final User user = ctx.user();
        if (user != null) {
            // proceed to AuthZ
            this.authorizeUser(ctx, user);
            return;
        }
        // parse the request in order to extract the credentials object
        this.parseCredentials(ctx, res -> {
            if (res.failed()) {
                this.processException(ctx, res.cause());
                return;
            }
            // check if the user has been set
            final User updatedUser = ctx.user();

            if (updatedUser != null) {
                final Session session = ctx.session();
                if (session != null) {
                    // the user has upgraded from unauthenticated to authenticated
                    // session should be upgraded as recommended by owasp
                    session.regenerateId();
                }
                // proceed to AuthZ
                this.authorizeUser(ctx, updatedUser);
                return;
            }
            // proceed to authN
            this.getAuthProvider(ctx).authenticate(AuthReady.prepare(res.result(), ctx), authN -> {
                if (authN.succeeded()) {
                    final User authenticated = authN.result();
                    ctx.setUser(authenticated);
                    final Session session = ctx.session();
                    if (session != null) {
                        // the user has upgraded from unauthenticated to authenticated
                        // session should be upgraded as recommended by owasp
                        session.regenerateId();
                        // Store user's princple into session
                    }
                    // proceed to AuthZ
                    this.authorizeUser(ctx, authenticated);
                } else {
                    // The first time to get
                    final String header = this.authenticateHeader(ctx);
                    if (header != null) {
                        ctx.response().putHeader("WWW-Authenticate", header);
                    }
                    // Zero extension for context error here
                    ctx.fail(null == authN.cause() ? this.UNAUTHORIZED : authN.cause());
                }
            });
        });
    }

    private void processException(final RoutingContext ctx, final Throwable exception) {
        if (null != exception) {
            if (exception instanceof WebException) {
                final WebException error = (WebException) exception;
                final HttpStatusCode statusCode = error.getStatus();
                final String payload = error.getMessage();

                final HttpServerResponse response = ctx.response();

                switch (statusCode) {
                    case MOVE_TEMPORARILY: {
                        response.putHeader(HttpHeaders.LOCATION, payload)
                                .setStatusCode(statusCode.code())
                                .end("Redirecting to " + payload + ".");
                    }
                    return;
                    case UNAUTHORIZED: {
                        final String header = this.authenticateHeader(ctx);
                        if (null != header) {
                            response.putHeader("WWW-Authenticate", header);
                        }
                        ctx.fail(this.UNAUTHORIZED);
                    }
                    return;
                    default:
                        ctx.fail(error);
                        return;
                }
            }
        }
        // Fallback 500
        ctx.fail(new _500InternalServerException(this.getClass(),
                null == exception ? null : exception.getMessage()));
    }

    private void authorizeUser(final RoutingContext ctx, final User user) {
        /*
         * A critical point is that here are secondary authorization of 401 workflow
         * we recommend to enable `permission` cache to do 403 workflow based on
         * 401 result.
         *
         * After 3.9.1 this feature should be OK, wait for testing
         */
        this.authProvider.authenticate(user.principal(), processed -> {
            if (processed.succeeded()) {
                this.authorize(processed.result(), authZ -> {
                    if (authZ.failed()) {
                        this.processException(ctx, authZ.cause());
                        return;
                    }
                    // success, allowed to continue
                    ctx.next();
                });
            } else {
                // 403 authorize failure
                final Throwable ex = processed.cause();
                if (Objects.nonNull(ex)) {
                    ctx.fail(ex);
                } else {
                    ctx.fail(FORBIDDEN);
                }
            }
        });
    }

    private boolean handlePreflight(final RoutingContext ctx) {
        final HttpServerRequest request = ctx.request();
        // See: https://www.w3.org/TR/cors/#cross-origin-request-with-preflight-0
        // Preflight requests should not be subject to security due to the reason UAs will remove the Authorization header
        if (request.method() == HttpMethod.OPTIONS) {
            // check if there is a access control request header
            final String accessControlRequestHeader = ctx.request().getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
            if (accessControlRequestHeader != null) {
                // lookup for the Authorization header
                for (final String ctrlReq : accessControlRequestHeader.split(Strings.COMMA)) {
                    if (ctrlReq.equalsIgnoreCase(HttpHeaders.AUTHORIZATION.toString())) {
                        // this request has auth in access control, so we can allow preflighs without authentication
                        ctx.next();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private AuthProvider getAuthProvider(final RoutingContext ctx) {
        try {
            final AuthProvider provider = ctx.get(AUTH_PROVIDER_CONTEXT_KEY);
            if (provider != null) {
                // we're overruling the configured one for this request
                this.authProvider = provider;
                return provider;
            }
        } catch (final RuntimeException e) {
            // bad type, ignore and return default
            e.printStackTrace();
        }
        return this.authProvider;
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }
}
