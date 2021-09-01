package io.vertx.tp.ke.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.Session;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.element.TypeAtom;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class Ke {

    /*
     * Read jooq configuration database name `catalog`
     */
    public static String getDatabase() {
        return KeTool.getCatalog();
    }

    public static boolean isIn(final JsonObject json, final String... fields) {
        return KeIs.isIn(json, fields);
    }


    public static <T> Future<T> poolAsync(final String name, final String key, final Supplier<Future<T>> supplier) {
        return KeTool.poolAsync(name, key, supplier);
    }

    public static Future<JsonArray> combineAsync(final JsonArray data, final ConcurrentMap<String, String> headers) {
        return KeExcel.combineAsync(data, headers);
    }

    public static Future<JsonArray> combineAsync(final JsonArray data, final ConcurrentMap<String, String> headers,
                                                 final List<String> columns) {
        return KeExcel.combineAsync(data, headers, columns, null);
    }

    public static Future<JsonArray> combineAsync(final JsonArray data, final ConcurrentMap<String, String> headers,
                                                 final List<String> columns,
                                                 final TypeAtom TypeAtom) {
        return KeExcel.combineAsync(data, headers, columns, TypeAtom);
    }

    public static Function<JsonObject, Future<JsonObject>> fabricAsync(final String field) {
        return KeCompare.combineAsync(field);
    }

    public static void banner(final String module) {
        KeTool.banner(module);
    }

    public static void infoKe(final Annal logger, final String pattern, final Object... args) {
        KeLog.infoKe(logger, pattern, args);
    }

    public static void debugKe(final Annal logger, final String pattern, final Object... args) {
        KeLog.debugKe(logger, pattern, args);
    }

    public static void runString(final Supplier<String> supplier, final Consumer<String> consumer) {
        KeTool.consume(supplier, consumer);
    }

    public static void runBoolean(final Supplier<Boolean> supplier, final Consumer<Boolean> consumer) {
        KeTool.consume(supplier, consumer);
    }

    public static void runInteger(final Supplier<Integer> supplier, final Consumer<Integer> consumer) {
        KeTool.consume(supplier, consumer);
    }

    public static <T, O> Future<O> channel(final Class<T> clazz, final Supplier<O> supplier,
                                           final Function<T, Future<O>> executor) {
        return KeRun.channel(clazz, supplier, executor);
    }

    public static <T, O> O channelSync(final Class<T> clazz, final Supplier<O> supplier,
                                       final Function<T, O> executor) {
        return KeRun.channelSync(clazz, supplier, executor);
    }

    public static <T, O> Future<O> channelAsync(final Class<T> clazz, final Supplier<Future<O>> supplier,
                                                final Function<T, Future<O>> executor) {
        return KeRun.channelAsync(clazz, supplier, executor);
    }

    /*
     * Session key generation
     */
    public static String keySession(final String method, final String uri, final Vis view) {
        return KeCache.keySession(method, uri, view);
    }

    public static String keyAuthorized(final String method, final String uri) {
        return KeCache.keyAuthorized(method, uri);
    }

    public static String keyHabitus(final Envelop envelop) {
        return KeCache.keyHabitus(envelop);
    }

    public static String keyUser(final Envelop envelop) {
        return KeCache.keyUser(envelop);
    }

    public static String keyUser(final User user) {
        return KeCache.keyUser(user);
    }

    /*
     * Get keySession here for current logged user
     */
    public static Future<Session> session(final String id) {
        return KeCache.session(id);
    }

    public static <T> Future<T> session(final Session session, final String sessionKey, final String dataKey, final T value) {
        return KeCache.session(session, sessionKey, dataKey, value);
    }

    public static Apt compmared(final Apt apt, final String user) {
        return KeCompare.compared(apt, KName.CODE, user);
    }

    public static Apt compmared(final Apt apt, final String field, final String user) {
        return KeCompare.compared(apt, field, user);
    }

    public static BiFunction<Function<JsonArray, Future<JsonArray>>, Function<JsonArray, Future<JsonArray>>, Future<JsonArray>> atomyFn(final Class<?> clazz, final Apt compared) {
        return KeCompare.atomyFn(clazz, compared);
    }

    /*
     * Result for response
     */
    public interface Result {

        static Future<JsonObject> boolAsync(final boolean checked) {
            return Ux.future(bool(checked));
        }

        static Future<Boolean> boolAsync(final JsonObject checkedJson) {
            return Ux.future(KeResult.bool(checkedJson));
        }

        static Future<JsonObject> jsonAsync(final JsonObject result) {
            return Ux.future(Ut.isNil(result) ? new JsonObject() : result);
        }

        static JsonObject bool(final boolean checked) {
            return KeResult.bool(KName.RESULT, checked);
        }

        static JsonObject bool(final String key, final boolean checked) {
            return KeResult.bool(key, checked);
        }

        static JsonObject array(final JsonArray array) {
            return KeResult.array(array);
        }
    }
}
