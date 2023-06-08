package io.vertx.up.fn;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 返回值是 Future 走 If 通道
 *
 * @author lang : 2023/4/30
 */
class _If extends _Combine {

    // JsonArray -> json
    public static Future<JsonObject> ifJArray(final String field, final JsonArray data) {
        return Future.succeededFuture(Ut.endJObject(field, data));
    }

    public static Future<JsonObject> ifJObject(final String field, final JsonObject data) {
        return Future.succeededFuture(Ut.endJObject(field, data));
    }

    public static Future<JsonObject> ifJArray(final JsonArray data) {
        return Future.succeededFuture(Ut.endJObject(KName.DATA, data));
    }

    // 默认值同步
    public static <I, T> Function<I, Future<T>> ifNull(final Supplier<T> supplier, final Supplier<T> executor) {
        return ifNull(supplier, (i) -> executor.get() /* Function */);
    }

    public static <I, T> Function<I, Future<T>> ifNull(final Supplier<T> supplier, final Function<I, T> executor) {
        return input -> Of.ofNull(() -> Future.succeededFuture(supplier.get()), executor).apply(input);
    }

    public static <I, T> Function<I, Future<T>> ifNil(final Supplier<T> supplier, final Supplier<Future<T>> executor) {
        return ifNil(supplier, (i) -> executor.get() /* Function */);
    }


    public static <I> Function<I, Future<JsonObject>> ifJObject(final Supplier<JsonObject> executor) {
        return _Of.ofJObject(() -> Future.succeededFuture(executor.get()));
    }

    public static <I> Function<I, Future<JsonObject>> ifJObject(final Function<I, JsonObject> executor) {
        return _Of.ofJObject(item -> Future.succeededFuture(executor.apply(item)));
    }

    public static Future<JsonObject> ifJArray(final JsonArray data, final JsonObject config) {
        return Future.succeededFuture(End.endJObject(KName.DATA, data, config));
    }

    public static Future<JsonObject> ifJArray(final String field, final JsonArray data, final JsonObject config) {
        return Future.succeededFuture(End.endJObject(field, data, config));
    }

    public static <I> Function<I, Future<JsonArray>> ifJArray(final Supplier<JsonArray> executor) {
        return _Of.ofJArray(() -> Future.succeededFuture(executor.get()));
    }

    public static <I> Function<I, Future<JsonArray>> ifJArray(final Function<I, JsonArray> executor) {
        return _Of.ofJArray(item -> Future.succeededFuture(executor.apply(item)));
    }

    public static <I, T> Function<I, Future<T>> ifNil(final Supplier<T> supplier, final Function<I, Future<T>> executor) {
        return input -> _Of.ofNil(() -> Future.succeededFuture(supplier.get()), executor).apply(input);
    }

    public static Future<Boolean> ifBool(final JsonObject input, final String field) {
        return Future.succeededFuture(Ut.endBool(input, field));
    }

    public static Future<Boolean> ifBool(final JsonObject input) {
        return Future.succeededFuture(Ut.endBool(input, KName.RESULT));
    }

    public static Future<JsonObject> ifBool(final boolean result, final String field) {
        return Future.succeededFuture(Ut.endBool(result, field));
    }

    public static Future<JsonObject> ifBool(final boolean result) {
        return Future.succeededFuture(Ut.endBool(result, KName.RESULT));
    }
    
    public static Future<JsonObject> ifWebUi(final JsonObject json, final String field) {
        return End.endWebUi(json, field);
    }
}
