package io.vertx.tp.crud.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxMeta;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.List;
import java.util.function.Function;

public class Ix {
    // Is --- Checking the result, return boolean
    /*
     * is existing for result
     */
    public static boolean isExist(final JsonObject result) {
        return IxIs.isExist(result);
    }

    // Business Logical
    /*
     * auditor setting
     */
    public static void audit(final JsonObject auditor, final JsonObject config, final String userId) {
        IxFn.audit(auditor, config, userId);
    }

    /*
     * search operation
     */
    public static Function<UxJooq, Future<JsonObject>> search(final JsonObject filters, final IxModule config) {
        return IxFn.search(filters, config);
    }

    public static Function<UxJooq, Future<JsonObject>> query(final JsonObject filters, final IxModule config) {
        return IxFn.query(filters, config);
    }

    public static Function<UxJooq, Future<Boolean>> existing(final JsonObject filters, final IxModule config) {
        return IxFn.existing(filters, config);
    }

    public static Future<JsonArray> query(final JsonArray data) {
        return IxFn.queryResult(data);
    }

    // Atom creation
    /*
     * IxIn reference
     */
    public static IxMeta create(final Class<?> clazz) {
        return IxMeta.create(clazz);
    }

    // Serialization for entity/list
    /*
     * analyze unique record
     */
    public static Future<JsonObject> unique(final JsonObject result) {
        return Ux.future(IxSerialize.unique(result));
    }

    public static Future<JsonArray> list(final JsonObject result) {
        return Ux.future(IxSerialize.list(result));
    }

    /*
     * Deserialize to T
     */
    public static <T> Future<T> entityAsync(final JsonObject data, final IxModule config) {
        final T reference = IxSerialize.entity(data, config);
        return Ux.future(reference);
    }

    public static <T> Future<List<T>> entityAsync(final JsonArray data, final IxModule config) {
        return Ux.future(IxSerialize.entity(data, config));
    }

    public static Future<JsonArray> zipperAsync(final JsonArray from, final JsonArray to, final IxModule config) {
        return Ux.future(IxSerialize.zipper(from, to, config));
    }

    // JqTool
    public static Future<JsonObject> inKeys(final JsonArray array, final IxModule config) {
        return Ux.future(IxQuery.inKeys(array, config));
    }

    /*
     * Log
     */
    public static void infoInit(final Annal logger, final String pattern, final Object... args) {
        IxLog.infoInit(logger, pattern, args);
    }

    public static void infoRest(final Annal logger, final String pattern, final Object... args) {
        IxLog.infoRest(logger, pattern, args);
    }

    public static void debugRest(final Annal logger, final String pattern, final Object... args) {
        IxLog.debugRest(logger, pattern, args);
    }

    public static void warnRest(final Annal logger, final String pattern, final Object... args) {
        IxLog.warnRest(logger, pattern, args);
    }

    public static void infoFilters(final Annal logger, final String pattern, final Object... args) {
        IxLog.infoFilters(logger, pattern, args);
    }

    public static void infoVerify(final Annal logger, final String pattern, final Object... args) {
        IxLog.infoVerify(logger, pattern, args);
    }

    public static void infoDao(final Annal logger, final String pattern, final Object... args) {
        IxLog.infoDao(logger, pattern, args);
    }

    public static void errorInit(final Annal logger, final String pattern, final Object... args) {
        IxLog.errorInit(logger, pattern, args);
    }
}
