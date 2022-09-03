package io.vertx.up.util;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

class Apply {

    static <I, T> Function<I, Future<T>> applyNil(final Supplier<T> supplier, final Supplier<Future<T>> executor) {
        return input -> {
            if (Objects.isNull(input)) {
                return Future.succeededFuture(supplier.get());
            } else {
                return executor.get();
            }
        };
    }

    static <I, T> Function<I, Future<T>> applyNil(final Supplier<T> supplier, final Function<I, Future<T>> executor) {
        return input -> {
            if (Objects.isNull(input)) {
                return Future.succeededFuture(supplier.get());
            } else {
                return executor.apply(input);
            }
        };
    }

    static <T> Function<T, Future<T>> applyNil(final Function<T, Future<T>> executor) {
        return input -> {
            if (Objects.isNull(input)) {
                return Future.succeededFuture(input);
            } else {
                return executor.apply(input);
            }
        };
    }

    static <T> Function<T[], Future<T[]>> applyEmpty(final Function<T[], Future<T[]>> executor) {
        return input -> {
            if (Objects.isNull(input) || 0 == input.length) {
                return Future.succeededFuture(input);
            } else {
                return executor.apply(input);
            }
        };
    }

    static Function<JsonArray, Future<JsonArray>> applyJEmpty(final Function<JsonArray, Future<JsonArray>> executor) {
        return input -> {
            if (Objects.isNull(input) || input.isEmpty()) {
                return Future.succeededFuture(input);
            } else {
                return executor.apply(input);
            }
        };
    }
}
