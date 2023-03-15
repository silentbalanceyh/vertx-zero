package io.vertx.up.fn;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 非空检查专用函数类
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class Wash {
    // ------------------------------- 异步处理 -----------------
    static <I, T> Function<I, Future<T>> ifNil(final Function<I, Future<T>> executor) {
        return input -> ifDefault(input, executor.apply(input));
    }

    static <I, T> Function<I, Future<T>> ifNil(final Supplier<Future<T>> supplier, final Function<I, Future<T>> executor) {
        return input -> {
            if (Objects.isNull(input)) {
                if (Objects.nonNull(supplier)) {
                    return supplier.get();
                } else {
                    return Future.succeededFuture();
                }
            }
            return ifNil(executor).apply(input);
        };
    }

    // ------------------------------- 同步处理 -----------------
    static <I, T> Function<I, Future<T>> ifNul(final Function<I, T> executor) {
        return input -> ifDefault(input, Future.succeededFuture(executor.apply(input)));
    }

    static <I, T> Function<I, Future<T>> ifNul(final Supplier<Future<T>> supplier, final Function<I, T> executor) {
        return input -> {
            if (Objects.isNull(input)) {
                if (Objects.nonNull(supplier)) {
                    return supplier.get();
                } else {
                    return Future.succeededFuture();
                }
            }
            return ifNul(executor).apply(input);
        };
    }

    static <T> Function<T[], Future<T[]>> ifEmpty(final Function<T[], Future<T[]>> executor) {
        return input -> {
            if (Objects.isNull(input) || 0 == input.length) {
                return Future.succeededFuture(input);
            }
            return executor.apply(input);
        };
    }

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
}
