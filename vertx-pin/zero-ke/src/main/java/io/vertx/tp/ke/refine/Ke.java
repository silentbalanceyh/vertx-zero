package io.vertx.tp.ke.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.commune.element.TypeAtom;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;

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
    public static String uri(final String uri, final String requestUri) {
        return KeCache.uri(uri, requestUri);
    }

    public static String uri(final RoutingContext context) {
        return KeCache.uri(context);
    }

    public static String keyView(final String method, final String uri, final Vis view) {
        return KeCache.keyView(method, uri, view);
    }

    public static String keyView(final RoutingContext context) {
        return KeCache.keyView(context);
    }

    public static String keyAuthorized(final String method, final String uri) {
        return KeCache.keyAuthorized(method, uri);
    }

    public static String keyResource(final String method, final String uri) {
        return KeCache.keyResource(method, uri);
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
}
