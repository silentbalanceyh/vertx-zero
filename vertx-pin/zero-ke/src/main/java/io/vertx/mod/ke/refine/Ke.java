package io.vertx.mod.ke.refine;

import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;
import io.macrocosm.specification.program.HArk;
import io.modello.specification.meta.HMetaAtom;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.commune.record.Apt;
import io.vertx.up.eon.KName;
import org.jooq.Configuration;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("all")
public class Ke extends _Um {

    /*
     * Jooq related:
     * Read jooq configuration database name `catalog`
     *
     * 1. getDatabase()
     * 2. getConfiguration()
     */
    public static String getDatabase() {
        return KeTool.getCatalog();
    }

    public static String getExtension(final String name) {
        Objects.requireNonNull(name);
        return "zero-" + name;
    }

    public static Configuration getConfiguration() {
        return KeTool.getConfiguration();
    }

    /*
     * Data Importing / Exporting Feature
     *
     * 1. combineAsync(JsonArray, ConcurrentMap<String,String>)
     * 2. combineAsync(JsonArray, ConcurrentMap<String,String>, List<String>)
     * 3. combineAsync(JsonArray, ConcurrentMap<String,String>, List<String>, TypeAtom)
     *
     * Dict Transfer Method
     * 1. fabricAsync(String field)
     */
    public static Future<JsonArray> combineAsync(final JsonArray data, final ConcurrentMap<String, String> headers) {
        return KeCompare.combineAsync(data, headers);
    }

    public static Future<JsonArray> combineAsync(final JsonArray data, final ConcurrentMap<String, String> headers,
                                                 final List<String> columns) {
        return KeCompare.combineAsync(data, headers, columns, null);
    }

    public static Future<JsonArray> combineAsync(final JsonArray data, final ConcurrentMap<String, String> headers,
                                                 final List<String> columns,
                                                 final HMetaAtom metaAtom) {
        return KeCompare.combineAsync(data, headers, columns, metaAtom);
    }

    /*
     * 1. mapFn(String, ConcurrentMap<String,JsonObject>, fn)
     * 2. mapFn(ConcurrentMap<String,JsonObject>, fn)
     */
    public static Function<JsonObject, Future<JsonObject>> mapFn(final String field, final ConcurrentMap<String, JsonObject> fieldConfig,
                                                                 final BiFunction<JsonObject, JsonArray, Future<JsonArray>> fileFn) {
        return data -> KeTool.map(data, field, fieldConfig, fileFn);
    }

    public static Function<JsonObject, Future<JsonObject>> mapFn(final ConcurrentMap<String, JsonObject> fieldConfig,
                                                                 final BiFunction<JsonObject, JsonArray, Future<JsonArray>> fileFn) {
        return data -> KeTool.map(data, KName.KEY, fieldConfig, fileFn);
    }

    public static <T, R> Future<R> mapApp(final Function<JsonObject, Future<T>> executor, final Function<Set<T>, Future<R>> combiner) {
        return KeTool.mapApp(executor, combiner);
    }


    /*
     * Banner Message
     * 1. banner(String)
     *
     * Log Method
     * 1. infoKe
     * 2. debugKe
     */
    public static void banner(final String module) {
        KeTool.banner(module);
    }

    /*
     * Session key generation
     *
     * 1. uri(String, String)
     * 2. uri(RoutingContext)
     *
     * Key Generation for different usage
     * 1. keyView(String, String, Vis)
     * 2. keyView(RoutingContext)
     * 3. keyAuthorized(String, String)
     * 4. keyResource(String, String)
     *
     */
    public static String uri(final String uri, final String requestUri) {
        return KeCache.uri(uri, requestUri);
    }

    public static String uri(final RoutingContext context) {
        return KeCache.uri(context);
    }

    /*
     * Comparing method
     * 1. compared(Apt, String)
     * 2. compared(Apt, String, String)
     * 3. atomyFn(Class<?>, Apt)
     */
    public static Apt compmared(final Apt apt, final String user) {
        return KeCompare.compared(apt, KName.CODE, user);
    }

    public static Apt compmared(final Apt apt, final String field, final String user) {
        return KeCompare.compared(apt, field, user);
    }

    public static BiFunction<Function<JsonArray, Future<JsonArray>>, Function<JsonArray, Future<JsonArray>>, Future<JsonArray>> atomyFn(final Class<?> clazz, final Apt compared) {
        return KeCompare.atomyFn(clazz, compared);
    }


    public static HArk ark(final MultiMap headers) {
        return KeApp.ark(headers);
    }

    public static HArk ark(final String value) {
        return KeApp.ark(value);
    }

    public static HArk ark() {
        return KeApp.ark();
    }


    public interface LOG {
        String MODULE = "Εισόδημα";
        LogModule Ke = Log.modulat(MODULE).program("Ke");
        LogModule Turnel = Log.modulat(MODULE).program("Channel");
    }
}
