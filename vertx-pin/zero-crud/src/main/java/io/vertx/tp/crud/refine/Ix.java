package io.vertx.tp.crud.refine;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.up.atom.Kv;
import io.vertx.up.experiment.mixture.HTAtom;
import io.vertx.up.experiment.modeling.KModule;
import io.vertx.up.experiment.specification.KField;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.uca.sectio.Aspect;
import io.vertx.up.unity.Ux;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Ix {
    // --------------------------------- New Version
    /*
     * createdBy, createdAt
     * updatedBy, updatedAt
     */
    public static void onAuditor(final JsonObject auditor, final JsonObject config, final String userId) {
        IxData.audit(auditor, config, userId);
    }

    /*
     * Column Parser
     * 1. metadata format
     * 2. string format
     * 3. dataIndex, title format
     */
    public static Kv<String, String> onColumn(final Object value) {
        return IxData.field(value);
    }

    /*
     * Impacting the uri when you save view
     */
    public static Kv<String, HttpMethod> onImpact(final IxMod in) {
        return IxData.impact(in);
    }

    /*
     * Get Unique Matrix rules
     */
    public static JsonArray onMatrix(final KField field) {
        return IxData.matrix(field);
    }

    /*
     * Get initial data
     */
    public static JsonObject onParameters(final IxMod in) {
        return IxData.parameters(in);
    }

    public static HTAtom onAtom(final IxMod active, final JsonArray columns) {
        return IxData.atom(active, columns);
    }

    // --------------------------------- Function Part
    public static Function<JsonObject, Future<JsonObject>> searchFn(final IxMod in) {
        return IxFn.searchFn(in);
    }

    public static Function<JsonObject, Future<Long>> countFn(final IxMod in) {
        return IxFn.countFn(in);
    }

    public static <T> BiFunction<Supplier<T>, BiFunction<UxJooq, JsonObject, Future<T>>, Future<T>> seekFn(final IxMod in, final Object json) {
        return IxFn.seekFn(in, json);
    }

    public static Function<JsonObject, Future<JsonArray>> fetchFn(final IxMod in) {
        return IxFn.fetchFn(in);
    }

    public static Function<JsonObject, Future<JsonObject>> fileFn(final IxMod in, final BiFunction<JsonObject, JsonArray, Future<JsonArray>> fileFn) {
        return IxFn.fileFn(in, fileFn);
    }

    // JqTool
    @SafeVarargs
    public static <T> Future<T> passion(final T input, final IxMod in, final BiFunction<T, IxMod, Future<T>>... executors) {
        return IxFn.passion(input, in, executors);
    }

    public static <T> Function<T, Future<T>> wrap(
        final KModule module, final BiFunction<Aspect, Function<T, Future<T>>, Function<T, Future<T>>> aopFn,
        final Function<T, Future<T>> executor) {
        return IxFn.wrap(module, aopFn, executor);
    }

    // --------------------------------- Serialization / Deserialization System
    public static <T> Future<T> deserializeT(final JsonObject data, final KModule config) {
        final T reference = IxSerialize.deserializeT(data, config);
        return Ux.future(reference);
    }

    public static <T> Future<List<T>> deserializeT(final JsonArray data, final KModule config) {
        return Ux.future(IxSerialize.deserializeT(data, config));
    }

    public static <T> JsonObject serializeJ(final T input, final KModule config) {
        return IxSerialize.serializeJ(input, config);
    }

    public static <T> JsonArray serializeA(final List<T> input, final KModule config) {
        return IxSerialize.serializeA(input, config);
    }

    public static JsonObject serializeP(final JsonObject pageData, final KModule active, final KModule standBy) {
        return IxSerialize.serializeP(pageData, active, standBy);
    }

    // --------------------------------- Logger Part
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

        public static void web(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IxLog.infoWeb(logger, pattern, args);
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
