package io.vertx.tp.rbac.refine;

import cn.vertxup.rbac.domain.tables.pojos.OAccessToken;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._401TokenCounterException;
import io.vertx.tp.error._401TokenExpiredException;
import io.vertx.tp.error._401TokenInvalidException;
import io.vertx.tp.rbac.atom.ScConfig;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.init.ScPin;
import io.vertx.up.atom.unity.UObject;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.WebException;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.horizon.eon.VValue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

class ScToken {
    private static final Annal LOGGER = Annal.get(ScToken.class);
    private static final ScConfig CONFIG = ScPin.getConfig();

    /*
     * Normalize Jwt Token data
     */
    static Future<Boolean> jwtToken(final List<OAccessToken> item, final String userId) {
        WebException error = null;
        if (null == item) {
            // Token Size
            Sc.infoAuth(LOGGER, AuthMsg.TOKEN_SIZE_NULL, item, userId);
            error = new _401TokenCounterException(ScToken.class, 0, userId);
        } else {
            if (item.isEmpty()) {
                // Token Size
                Sc.infoAuth(LOGGER, AuthMsg.TOKEN_SIZE_EMPTY, item, userId);
                error = new _401TokenCounterException(ScToken.class, 0, userId);
            } else if (1 < item.size()) {
                // Token Size
                Sc.infoAuth(LOGGER, AuthMsg.TOKEN_SIZE_MULTI, item, userId);
                error = new _401TokenCounterException(ScToken.class, item.size(), userId);
            } else {
                final OAccessToken token = item.get(Values.IDX);
                final String tokenString = new String(token.getToken(), VValue.DFT.CHARSET);
                final byte[] authBytes = userId.getBytes(VValue.DFT.CHARSET);
                if (Arrays.equals(authBytes, token.getAuth())) {
                    // Token expired
                    final long currentTime = new Date().getTime();
                    final long tokenTime = token.getExpiredTime();
                    if (tokenTime < currentTime) {
                        Sc.infoAuth(LOGGER, AuthMsg.TOKEN_EXPIRED, tokenString, tokenTime);
                        error = new _401TokenExpiredException(ScToken.class, tokenString);
                    }
                } else {
                    // Token invalid
                    Sc.infoAuth(LOGGER, AuthMsg.TOKEN_INVALID, tokenString);
                    error = new _401TokenInvalidException(ScToken.class, tokenString);
                }
            }
        }
        if (null == error) {
            return Ux.future(Boolean.TRUE);
        } else {
            return Future.failedFuture(error);
        }
    }

    static JsonObject jwtToken(final JsonObject data) {
        /* Token Data Extract */
        final JsonObject tokenData = UObject.create(data.copy())
            .remove("role", "group").to();
        Sc.infoAuth(LOGGER, AuthMsg.TOKEN_JWT, tokenData.encode());
        /* Token */
        final String token = Ux.Jwt.token(tokenData);

        /* Response */
        final JsonObject response = new JsonObject();
        response.put(AuthKey.ACCESS_TOKEN, token);

        /* Refresh Token */
        final String refreshToken = Ux.Jwt.token(response.copy());
        response.put(AuthKey.REFRESH_TOKEN, refreshToken);

        /* Token Expired */
        final Long iat = new Date().getTime() + CONFIG.getTokenExpired();
        response.put(AuthKey.IAT, iat);
        return response;
    }

    static OAccessToken jwtToken(final JsonObject jwt, final String userKey) {
        /* Token byte[] */
        final byte[] token = jwt.getString(AuthKey.ACCESS_TOKEN).getBytes(VValue.DFT.CHARSET);
        /* Refresh Token byte[] */
        final byte[] refreshToken = jwt.getString(AuthKey.REFRESH_TOKEN).getBytes(VValue.DFT.CHARSET);
        final Long iat = jwt.getLong(AuthKey.IAT);
        return new OAccessToken()
            .setKey(UUID.randomUUID().toString())
            .setAuth(userKey.getBytes(VValue.DFT.CHARSET))

            /* Created Auditor */
            .setCreatedBy(userKey)
            .setCreatedAt(LocalDateTime.now())

            /* Token Info */
            .setToken(token)
            .setRefreshToken(refreshToken)
            .setExpiredTime(iat)

            /* Active */
            .setActive(Boolean.TRUE);
    }
}
