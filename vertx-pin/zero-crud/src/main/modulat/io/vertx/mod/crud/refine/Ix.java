package io.vertx.mod.crud.refine;

import io.aeon.experiment.specification.KModule;
import io.horizon.atom.common.Kv;
import io.horizon.uca.aop.Aspect;
import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;
import io.modello.specification.meta.HMetaAtom;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.atom.shape.KField;
import io.vertx.up.uca.jooq.UxJooq;
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

    public static HMetaAtom onAtom(final IxMod active, final JsonArray columns) {
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
    public interface LOG {

        String MODULE = "Εκδήλωση";

        LogModule Filter = Log.modulat(MODULE).program("Filter");
        LogModule Init = Log.modulat(MODULE).program("Init");
        LogModule Rest = Log.modulat(MODULE).program("Rest");
        LogModule Web = Log.modulat(MODULE).program("Web");
        LogModule Dao = Log.modulat(MODULE).program("Dao");
        LogModule Verify = Log.modulat(MODULE).program("Verify");
    }
}
