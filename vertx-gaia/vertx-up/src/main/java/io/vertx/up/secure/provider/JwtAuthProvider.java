package io.vertx.up.secure.provider;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.jwt.JWT;
import io.vertx.up.eon.Constants;
import io.vertx.up.exception.web.*;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.secure.Security;
import io.vertx.up.unity.Ux;

import java.util.Collections;
import java.util.function.Supplier;

public class JwtAuthProvider implements JwtAuth {
    private static final JsonArray EMPTY_ARRAY = new JsonArray();
    private static final String AUTH_POOL = Constants.DEFAULT_JWT_AUTH_POOL;
    private static final Annal LOGGER = Annal.get(JwtAuthProvider.class);
    private final JWT jwt;
    private final String permissionsClaimKey;
    private final JWTOptions jwtOptions;
    private final transient JwtSecurer securer = JwtSecurer.create();

    private transient AsyncMap<String, Boolean> sessionTokens;

    JwtAuthProvider(final Vertx vertx, final JWTAuthOptions config) {
        this.permissionsClaimKey = config.getPermissionsClaimKey();
        // Set this key to securer
        this.securer.setPermissionsClaimKey(this.permissionsClaimKey);
        this.jwtOptions = config.getJWTOptions();
        // File reading here.
        this.jwt = Ux.Jwt.create(config, vertx.fileSystem()::readFileBlocking);
        vertx.sharedData().<String, Boolean>getAsyncMap(JwtAuthProvider.AUTH_POOL, res -> {
            if (res.succeeded()) {
                JwtAuthProvider.LOGGER.debug(Info.MAP_INITED, JwtAuthProvider.AUTH_POOL);
                this.sessionTokens = res.result();
            }
        });
    }

    @Override
    public JwtAuth bind(final Supplier<Security> supplier) {
        final Security security = supplier.get();
        /*
         * Zero Security Framework needed, could not break this rule.
         */
        Fn.outWeb(null == security, _500SecurityNotImplementException.class, this.getClass());
        this.securer.setSecurity(security);
        return this;
    }

    @Override
    public void authenticate(final JsonObject authInfo, final Handler<AsyncResult<User>> handler) {
        JwtAuthProvider.LOGGER.info("( Auth ) Auth Information: {0}", authInfo.encode());
        final String token = authInfo.getString("jwt");
        /*
         * Extract token from sessionTokens here
         */
        if (null == this.sessionTokens) {
            /*
             * Session tokens is null, it means zero system will disabled token cache future here.
             * It will go through common authenticate workflow here.
             * 1) 401 Validation
             * 2) 403 Validation ( If So )
             */
            JwtAuthProvider.LOGGER.debug(Info.FLOW_NULL, token);
            this.prerequisite(token)
                    /* 401 */
                    .compose(nil -> this.securer.authenticate(authInfo))
                    /* Mount Handler */
                    .onComplete(this.authorized(token, handler));
        } else {
            /*
             * Get token from sessionTokens
             */
            this.sessionTokens.get(token, res -> {
                if (null != res && null != res.result() && res.result()) {
                    /* Token verified from cache */
                    JwtAuthProvider.LOGGER.info(Info.MAP_HIT, token, res.result());
                    /*
                     * Also this situation, the prerequisite step could be skipped because it has
                     * been calculated before, the token is valid and we could process this token
                     * go to 403 workflow directly.
                     * 401 Validation Skip
                     * 403 Validation ( If So )
                     */
                    this.securer.authorize(authInfo)
                            /* Mount Handler */
                            .onComplete(this.authorized(token, handler));
                } else {
                    JwtAuthProvider.LOGGER.debug(Info.MAP_MISSING, token);
                    /*
                     * Repeat do 401 validation & 403 validation
                     * 1) 401 Validation
                     * 2) 403 Validation ( If So )
                     */
                    this.prerequisite(token)
                            /* 401 */
                            .compose(nil -> this.securer.authenticate(authInfo))
                            /* Mount Handler */
                            .onComplete(this.authorized(token, handler));
                }
            });
        }
    }

    private Handler<AsyncResult<User>> authorized(final String token, final Handler<AsyncResult<User>> handler) {
        return user -> {
            if (user.succeeded()) {
                /* Store token into async map to refresh cache and then return */
                this.sessionTokens.put(token, Boolean.TRUE, result -> {
                    JwtAuthProvider.LOGGER.debug(Info.MAP_PUT, token, Boolean.TRUE);
                    handler.handle(Future.succeededFuture(user.result()));
                });
            } else {
                /* Capture the result from internal calling */
                final Throwable error = user.cause();
                /* Debug for error stack here */
                Ux.debug(error, () -> error);
                handler.handle(Future.failedFuture(error));
            }
        };
    }

    /*
     * Prerequisite for JWT token here based on jwtOptions & jwt
     * This function is provided by zero system and it will verify jwt token specification only
     * It does not contain any business code logical here.
     */
    @SuppressWarnings("all")
    private Future<String> prerequisite(final String token) {
        try {
            final JsonObject payload = this.jwt.decode(token);
            if (this.jwt.isExpired(payload, this.jwtOptions)) {
                return Future.failedFuture(new _401JwtExpiredException(this.getClass(), payload));
            }
            if (this.jwtOptions.getAudience() != null) {
                final JsonArray target;
                if (payload.getValue("aud") instanceof String) {
                    target = (new JsonArray()).add(payload.getValue("aud", ""));
                } else {
                    target = payload.getJsonArray("aud", EMPTY_ARRAY);
                }

                if (Collections.disjoint(this.jwtOptions.getAudience(), target.getList())) {
                    return Future.failedFuture(new _401JwtAudientException(this.getClass(), Json.encode(this.jwtOptions.getAudience())));
                }
            }

            if (this.jwtOptions.getIssuer() != null && !this.jwtOptions.getIssuer().equals(payload.getString("iss"))) {
                return Future.failedFuture(new _401JwtIssuerException(this.getClass(), payload.getString("iss")));
            }
            return Future.succeededFuture(token);
        } catch (final RuntimeException ex) {
            return Future.failedFuture(new _500JwtRuntimeException(this.getClass(), ex));
        }
    }

    @Override
    public String generateToken(final JsonObject claims, final JWTOptions options) {
        final JsonObject _claims = claims.copy();
        if (options.getPermissions() != null && !_claims.containsKey(this.permissionsClaimKey)) {
            _claims.put(this.permissionsClaimKey, new JsonArray(options.getPermissions()));
        }
        return this.jwt.sign(_claims, options);
    }
}
