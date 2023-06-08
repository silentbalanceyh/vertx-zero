package io.vertx.up.fn;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author lang : 2023/5/1
 */
class Of {
    @SuppressWarnings("unchecked")
    private static <I, T> Future<T> ifDefault(final I input, final Future<T> future) {
        if (Objects.isNull(input)) {
            return Future.succeededFuture();
        }
        if (input instanceof final JsonArray array) {
            // JsonArray Null Checking
            if (array.isEmpty()) {
                final T emptyA = (T) new JsonArray();
                return Future.succeededFuture(emptyA);
            }
        } else if (input instanceof final JsonObject object) {
            // JsonObject Null Checking
            if (Ut.isNil(object)) {
                final T emptyJ = (T) new JsonObject();
                return Future.succeededFuture(emptyJ);
            }
        }
        return future;
    }

    static <T, V> Consumer<JsonObject> ofField(final String field, final Function<V, T> executor) {
        return input -> {
            final JsonObject inputJ = Ut.valueJObject(input);
            if (inputJ.containsKey(field)) {
                final Object value = inputJ.getValue(field);
                if (Objects.nonNull(value)) {
                    executor.apply((V) value);
                }
            }
        };
    }

    // ------------------------------- 异步处理 -----------------
    static <I, T> Function<I, Future<T>> ofNil(final Function<I, Future<T>> executor) {
        return input -> ifDefault(input, executor.apply(input));
    }

    static <I, T> Function<I, Future<T>> ofNil(final Supplier<Future<T>> supplier, final Function<I, Future<T>> executor) {
        return input -> {
            if (Objects.isNull(input)) {
                if (Objects.nonNull(supplier)) {
                    return supplier.get();
                } else {
                    return Future.succeededFuture();
                }
            }
            return ofNil(executor).apply(input);
        };
    }


    static <I, T> Function<I, Future<T>> ofNull(final Supplier<Future<T>> supplier, final Function<I, T> executor) {
        return input -> {
            if (Objects.isNull(input)) {
                if (Objects.nonNull(supplier)) {
                    return supplier.get();
                } else {
                    return Future.succeededFuture();
                }
            }
            return ofNull(executor).apply(input);
        };
    }

    static <I, T> Function<I, Future<T>> ofNull(final Function<I, T> executor) {
        return input -> ifDefault(input, Future.succeededFuture(executor.apply(input)));
    }

    static <T> Function<T[], Future<T[]>> ofEmpty(final Function<T[], Future<T[]>> executor) {
        return input -> {
            if (Objects.isNull(input) || 0 == input.length) {
                return Future.succeededFuture(input);
            }
            return executor.apply(input);
        };
    }


    /*
     * Item:  field = input
     * mount ----->  mount +        ------> mount
     *               field = input
     */
    static <T> Function<T, JsonObject> ofJObject(final String field, final JsonObject mount) {
        return input -> {
            // 默认返回引用
            final JsonObject mountJ = Ut.valueJObject(mount);
            if (Objects.nonNull(input)) {
                mountJ.put(field, input);
            }
            return mountJ;
        };
    }

    /*
     * Item:  field = T
     * mount --->  mount -> t    ------> mount
     *             t -> json
     *             field = json
     */
    @SuppressWarnings("unchecked")
    static <T> Function<JsonObject, Future<JsonObject>> ofJObject(final String field, final Function<T, Future<JsonObject>> executor) {
        return mount -> {
            if (Ut.isNil(field) ||
                !mount.containsKey(field) ||
                Objects.isNull(executor)) {
                // Nothing
                return Future.succeededFuture(mount);
            }
            final T value = (T) mount.getValue(field);
            if (Objects.isNull(value)) {
                // Nothing
                return Future.succeededFuture(mount);
            }
            // Function Processing
            return executor.apply(value).compose(data -> {
                if (Ut.isNotNil(data)) {
                    mount.put(field, data);
                }
                return Future.succeededFuture(mount);
            });
        };
    }
}
