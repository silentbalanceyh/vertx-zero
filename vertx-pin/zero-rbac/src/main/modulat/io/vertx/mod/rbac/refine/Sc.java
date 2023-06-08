package io.vertx.mod.rbac.refine;

import cn.vertxup.rbac.domain.tables.pojos.*;
import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.specification.secure.Acl;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Sc {

    /*
     * cache information
     * 1. Code: Authorization Code Cache Pool
     *    - get data from code cache
     *    - put data into code cache
     */
    public static <V> Future<V> cacheCode(final String key) {
        return ScCache.code(key);
    }

    public static <V> Future<V> cacheCode(final String key, final V value) {
        return ScCache.code(key, value);
    }

    public static Future<JsonObject> cachePath(final SPath path, final Function<SPath, Future<JsonObject>> executor) {
        return ScCache.admitPath(path, executor, "PATH");
    }

    public static Future<List<SPacket>> cachePocket(final SPath path, final Function<SPath, Future<List<SPacket>>> executor) {
        return ScCache.admitPath(path, executor, "POCKET");
    }

    public static Future<JsonObject> cacheView(final RoutingContext context, final String habitus) {
        return ScCache.view(context, habitus);
    }

    /*
     * Business logical
     * Generate authorization code based on `configuration.json` file
     * - codeExpired
     * - codeLength
     * - codePool
     */
    public static String valueCode() {
        return ScCache.valueCode();
    }

    public static String valuePassword() {
        return ScCache.valuePassword();
    }

    public static String valueProfile(final SResource resource) {
        return ScCache.valueProfile(resource);
    }

    public static Future<OUser> valueAuth(final SUser user, final JsonObject inputJ) {
        return ScCache.valueAuth(user, inputJ);
    }

    public static Future<List<OUser>> valueAuth(final List<SUser> users) {
        return ScCache.valueAuth(users);
    }

    public static Future<List<SUser>> valueAuth(final JsonArray userA, final String sigma) {
        return ScCache.valueAuth(userA, sigma);
    }

    /*
     * Lock Part
     * - lockOn, when handler, the counter increased
     * - lockOff, when success, the counter cleared
     * - lockVerify, when before login, verify the specification first
     */
    public static Future<JsonObject> lockVerify(final String username, final Supplier<Future<JsonObject>> executor) {
        return ScCache.lockVerify(username, executor);
    }

    public static Future<Integer> lockOn(final String username) {
        return ScCache.lockOn(username);
    }

    public static Future<Integer> lockOff(final String username) {
        return ScCache.lockOff(username);
    }

    /*
     * Image Part
     */
    public static Future<Buffer> imageOn(final String sessionId, final int width, final int height) {
        return ScCache.imageOn(sessionId, width, height);
    }

    public static <T> Future<T> imageVerify(final String sessionId,
                                            final JsonObject params,
                                            final Function<JsonObject, Future<T>> executor) {
        return ScCache.imageVerify(sessionId, params, executor);
    }

    public static Future<Boolean> imageOff(final String sessionId) {
        return ScCache.imageKo(sessionId);
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

    public interface LOG {
        String MODULE = "Ακριβώς";

        LogModule Auth = Log.modulat(MODULE).program("Auth");
        LogModule Web = Log.modulat(MODULE).program("Web");
        LogModule View = Log.modulat(MODULE).program("View");
        LogModule Visit = Log.modulat(MODULE).program("Visit");
        LogModule Resource = Log.modulat(MODULE).program("Resource");
        LogModule Init = Log.modulat(MODULE).program("Init");
        LogModule Credit = Log.modulat(MODULE).program("Credit");
        LogModule Audit = Log.modulat(MODULE).program("Audit");
    }
}
