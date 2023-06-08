package io.vertx.up.fn;

import io.horizon.util.HUt;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 返回值是 Function 走 Of 通道
 *
 * @author lang : 2023/4/30
 */
class _Of extends _If {

    public static Function<JsonObject, Future<JsonObject>> ofCopy(final String from, final String to) {
        return json -> Future.succeededFuture(Ut.valueCopy(json, from, to));
    }

    public static Function<JsonObject, Future<JsonObject>> ofCopy(final JsonObject source, final String... fields) {
        return target -> Future.succeededFuture(Ut.valueCopy(target, source, fields));
    }

    public static Function<JsonObject, Future<JsonObject>> ofDefault(final String field, final Object value) {
        return record -> Future.succeededFuture(Ut.valueDefault(record, field, value));
    }

    public static Function<JsonObject, Future<JsonObject>> ofJObject(final String... fields) {
        return json -> Future.succeededFuture(Ut.valueToJObject(json, fields));
    }

    // json -> data( field = json )
    public static <T> Function<T, Future<JsonObject>> ofJObject(final String field, final JsonObject data) {
        return t -> Future.succeededFuture(Of.ofJObject(field, data).apply(t));
    }

    // json -> json ( field = data )
    public static <T> Function<JsonObject, Future<JsonObject>> ofJObject(
        final String field, final Function<T, Future<JsonObject>> executor) {
        return Of.ofJObject(field, executor);
    }

    public static Function<JsonArray, Future<JsonArray>> ofJArray(final String... fields) {
        return array -> Future.succeededFuture(Ut.valueToJArray(array, fields));
    }


    public static Function<JsonObject, Future<JsonObject>> ofStringJ(final String... fields) {
        return json -> Future.succeededFuture(Ut.valueToString(json, fields));
    }

    public static Function<JsonArray, Future<JsonArray>> ofStringA(final String... fields) {
        return array -> Future.succeededFuture(Ut.valueToString(array, fields));
    }

    public static Function<JsonObject, Future<JsonObject>> ofPage(final String... fields) {
        return pageData -> Future.succeededFuture(Ut.valueToPage(pageData, fields));
    }

    // 合并专用方法（三个重载）
    public static Function<JsonObject, Future<JsonObject>> ofMerge(final JsonObject input) {
        return t -> Future.succeededFuture(HUt.valueMerge(input, t));
    }

    public static <T> Function<T, Future<JsonObject>> ofMerge(final JsonObject input, final String field) {
        return t -> Future.succeededFuture(HUt.valueMerge(input, field, t));
    }

    public static <T, V> Consumer<JsonObject> ofMerge(final String field, final Function<V, T> executor) {
        return Of.ofField(field, executor);
    }

    public static <I> Function<I, Future<JsonArray>> ofJArray(final Function<I, Future<JsonArray>> executor) {
        return ofNil(() -> Future.succeededFuture(new JsonArray()), executor);
    }

    public static <I> Function<I, Future<JsonArray>> ofJArray(final Supplier<Future<JsonArray>> executor) {
        return ofJArray(i -> executor.get());
    }

    public static <I, T> Function<I, Future<T>> ofNil(final Supplier<Future<T>> supplier, final Supplier<Future<T>> executor) {
        return ofNil(supplier, i -> executor.get() /* Function */);
    }

    public static <I, T> Function<I, Future<T>> ofNil(final Supplier<Future<T>> supplier, final Function<I, Future<T>> executor) {
        return Of.ofNil(supplier, executor);
    }

    // 变种（全异步，默认值同步）JsonObject
    public static <I> Function<I, Future<JsonObject>> ofJObject(final Function<I, Future<JsonObject>> executor) {
        return ofNil(() -> Future.succeededFuture(new JsonObject()), executor);
    }

    public static <I> Function<I, Future<JsonObject>> ofJObject(final Supplier<Future<JsonObject>> executor) {
        return ofJObject(i -> executor.get());
    }

    public static <I, T> Function<I, Future<T>> ofNil(final Function<I, Future<T>> executor) {
        return Of.ofNil(executor);
    }

    // 默认值异步
    public static <I, T> Function<I, Future<T>> ofNull(final Function<I, T> executor) {
        return Of.ofNull(executor);
    }

    public static <I, T> Function<I, Future<T>> ofNull(final Supplier<Future<T>> supplier, final Supplier<T> executor) {
        return ofNull(supplier, i -> executor.get() /* Function */);
    }

    public static <I, T> Function<I, Future<T>> ofNull(final Supplier<Future<T>> supplier, final Function<I, T> executor) {
        return Of.ofNull(supplier, executor);
    }

    public static <T> Function<T[], Future<T[]>> ofEmpty(final Function<T[], Future<T[]>> executor) {
        return Of.ofEmpty(executor);
    }

    public static <T> Function<JsonObject, Future<JsonObject>> ofTree(
        final String field, final Function<T, Future<JsonObject>> executor) {
        return End.endTree(field, false, executor);
    }

    public static <T> Function<JsonObject, Future<JsonObject>> ofTree(
        final String field, final boolean deeply, final Function<T, Future<JsonObject>> executor) {
        return End.endTree(field, deeply, executor);
    }

    public static Function<JsonObject, Future<JsonObject>> ofWebUi(final String field) {
        return json -> ifWebUi(json, field);
    }
}
