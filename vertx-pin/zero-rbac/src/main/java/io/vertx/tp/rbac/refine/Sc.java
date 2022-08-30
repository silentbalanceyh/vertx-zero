package io.vertx.tp.rbac.refine;

import cn.vertxup.rbac.domain.tables.pojos.OAccessToken;
import cn.vertxup.rbac.domain.tables.pojos.OUser;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.secure.Acl;
import io.vertx.up.log.Annal;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Sc {
    /*
     * Log information with input Logger
     */
    public static void infoAuth(final Annal logger, final String pattern, final Object... args) {
        ScLog.infoAuth(logger, pattern, args);
    }

    public static void warnAuth(final Annal logger, final String pattern, final Object... args) {
        ScLog.warnAuth(logger, pattern, args);
    }

    public static void debugAuth(final Annal logger, final String pattern, final Object... args) {
        ScLog.debugAuth(logger, pattern, args);
    }

    public static void infoAudit(final Annal logger, final String pattern, final Object... args) {
        ScLog.infoAudit(logger, pattern, args);
    }

    public static void infoInit(final Annal logger, final String pattern, final Object... args) {
        ScLog.infoInit(logger, pattern, args);
    }

    public static void infoResource(final Annal logger, final String pattern, final Object... args) {
        ScLog.infoResource(logger, pattern, args);
    }

    public static void debugCredit(final Annal logger, final String pattern, final Object... args) {
        ScLog.debugCredit(logger, pattern, args);
    }

    public static void infoAuth(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal LOGGER = Annal.get(clazz);
        ScLog.infoAuth(LOGGER, pattern, args);
    }

    public static void infoWeb(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal LOGGER = Annal.get(clazz);
        ScLog.infoWeb(LOGGER, pattern, args);
    }

    public static void warnWeb(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal LOGGER = Annal.get(clazz);
        ScLog.warnWeb(LOGGER, pattern, args);
    }

    public static void infoView(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal LOGGER = Annal.get(clazz);
        ScLog.infoView(LOGGER, pattern, args);
    }

    /*
     * cache information
     * 1. Code: Authorization Code Cache Pool
     *    - get data from code cache
     *    - put data into code cache
     */
    public static <V> Future<V> cacheCode(final String key) {
        return ScTool.code(key);
    }

    public static <V> Future<V> cacheCode(final String key, final V value) {
        return ScTool.code(key, value);
    }

    /*
     * Business logical
     * Generate authorization code based on `configuration.json` file
     * - codeExpired
     * - codeLength
     * - codePool
     */
    public static String valueCode() {
        return ScTool.valueCode();
    }

    public static String valuePassword() {
        return ScTool.valuePassword();
    }

    public static String valueProfile(final SResource resource) {
        return ScTool.valueProfile(resource);
    }

    public static Future<OUser> valueAuth(final SUser user, final JsonObject inputJ) {
        return ScTool.valueAuth(user, inputJ);
    }

    public static Future<List<OUser>> valueAuth(final List<SUser> users) {
        return ScTool.valueAuth(users);
    }

    public static Future<List<SUser>> valueAuth(final JsonArray userA, final String sigma) {
        return ScTool.valueAuth(userA, sigma);
    }

    /*
     * Lock Part
     * - lockOn, when failure, the counter increased
     * - lockOff, when success, the counter cleared
     * - lockVerify, when before login, verify the specification first
     */
    public static Future<JsonObject> lockVerify(final String username, final Supplier<Future<JsonObject>> executor) {
        return ScTool.lockVerify(username, executor);
    }

    public static Future<Integer> lockOn(final String username) {
        return ScTool.lockOn(username);
    }

    public static Future<Integer> lockOff(final String username) {
        return ScTool.lockOff(username);
    }

    /*
     * Image Part
     */
    public static Future<Buffer> imageOn(final String sessionId, final int width, final int height) {
        return ScTool.imageOn(sessionId, width, height);
    }

    public static <T> Future<T> imageVerify(final String sessionId,
                                            final JsonObject params,
                                            final Function<JsonObject, Future<T>> executor) {
        return ScTool.imageVerify(sessionId, params, executor);
    }

    public static Future<Boolean> imageOff(final String sessionId) {
        return ScTool.imageKo(sessionId);
    }

    /*
     * Jwt token process
     * 1) Build jwt token response
     * {
     *     access_token: "xxx",
     *     refresh_token: "xxx",
     *     iat: xx
     * }
     * 2) Build OAccessToken object, this object will be stored into database.
     */
    public static JsonObject jwtToken(final JsonObject data) {
        return ScToken.jwtToken(data);
    }

    public static Future<Boolean> jwtToken(final List<OAccessToken> item, final String userId) {
        return ScToken.jwtToken(item, userId);
    }

    public static OAccessToken jwtToken(final JsonObject jwt, final String userKey) {
        return ScToken.jwtToken(jwt, userKey);
    }

    /*
     * Acl method
     */
    public static JsonArray aclOn(final JsonArray original, final Acl acl) {
        return ScAcl.aclOn(original, acl);
    }

    public static void aclRecord(final JsonObject record, final Acl acl) {
        ScAcl.aclRecord(record, acl);
    }
}
