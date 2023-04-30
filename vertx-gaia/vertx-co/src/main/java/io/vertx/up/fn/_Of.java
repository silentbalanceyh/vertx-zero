package io.vertx.up.fn;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.function.Function;

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
}
