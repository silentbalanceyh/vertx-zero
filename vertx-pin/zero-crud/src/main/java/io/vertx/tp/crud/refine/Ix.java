package io.vertx.tp.crud.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxProc;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Ix {
    // Business Logical
    public static void audit(final JsonObject auditor, final JsonObject config, final String userId) {
        IxData.audit(auditor, config, userId);
    }

    // Is --- Checking the result, return boolean
    /*
     * is existing for result
     */
    public static boolean isExist(final JsonObject result) {
        return IxFn.isExist(result);
    }

    public static Function<JsonObject, Future<JsonObject>> searchFn(final IxIn in) {
        return IxQuery.search(in);
    }

    public static Function<JsonObject, Future<Long>> countFn(final IxIn in) {
        return IxQuery.count(in);
    }

    /*
     * search operation
     */
    public static Function<UxJooq, Future<JsonObject>> search(final JsonObject filters, final KModule config) {
        return IxQuery.search(filters, config);
    }

    public static Function<UxJooq, Future<Boolean>> existing(final JsonObject filters, final KModule config) {
        return IxQuery.existing(filters, config);
    }

    public static Function<UxJooq, Future<JsonObject>> query(final JsonObject filters, final KModule config) {
        return IxQuery.query(filters, config);
    }

    // Atom creation
    /*
     * IxIn reference
     */
    public static IxProc create(final Class<?> clazz) {
        return IxProc.create(clazz);
    }

    // Serialization for entity/list
    /*
     * analyze unique record
     */
    public static Future<JsonObject> serializePO(final JsonObject result, final KModule config) {
        return Ux.future(IxSerialize.serializePO(result, config));
    }

    public static Future<JsonArray> serializePL(final JsonObject result, final KModule config) {
        return Ux.future(IxSerialize.serializePL(result, config));
    }

    /*
     * Deserialize to T
     */
    public static <T> Future<T> deserializeT(final JsonObject data, final KModule config) {
        final T reference = IxSerialize.deserializeT(data, config);
        return Ux.future(reference);
    }

    public static <T> Future<List<T>> deserializeT(final JsonArray data, final KModule config) {
        return Ux.future(IxSerialize.deserializeT(data, config));
    }

    public static Future<JsonArray> serializeA(final JsonArray data, final KModule config) {
        return Ux.future(IxSerialize.serializeA(data, config));
    }

    public static Future<JsonObject> serializeJ(final JsonObject data, final KModule config) {
        return Ux.future(IxSerialize.serializeJ(data, config));
    }

    public static Future<JsonArray> serializeA(final JsonArray from, final JsonArray to, final KModule config) {
        return Ux.future(IxSerialize.serializeA(from, to, config));
    }


    public static Future<JsonObject> inKeys(final JsonArray array, final KModule config) {
        return Ux.future(IxQuery.inKeys(array, config));
    }

    // JqTool
    @SafeVarargs
    public static <T> Future<T> passion(final T input, final IxIn in, final BiFunction<T, IxIn, Future<T>>... executors) {
        return IxFn.passion(input, in, executors);
    }

    /*
     * Log
     */
    public static void infoFilters(final Annal logger, final String pattern, final Object... args) {
        IxLog.infoFilters(logger, pattern, args);
    }

    public static void infoDao(final Annal logger, final String pattern, final Object... args) {
        IxLog.infoDao(logger, pattern, args);
    }

    public static class Log {

        public static void filters(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IxLog.infoFilters(logger, pattern, args);
        }

        public static void init(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IxLog.infoInit(logger, pattern, args);
        }

        public static void rest(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IxLog.infoRest(logger, pattern, args);
        }

        public static void restW(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IxLog.warnRest(logger, pattern, args);
        }

        public static void dao(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IxLog.infoDao(logger, pattern, args);
        }

        public static void verify(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IxLog.infoVerify(logger, pattern, args);
        }
    }
}
