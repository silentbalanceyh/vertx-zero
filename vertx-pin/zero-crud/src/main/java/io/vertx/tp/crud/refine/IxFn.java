package io.vertx.tp.crud.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxIn;

import java.util.Objects;
import java.util.function.BiFunction;

class IxFn {

    static boolean isExist(final JsonObject result) {
        final Long counter = result.getLong("count", 0L);
        return 0 < counter;
    }

    // JqFn
    @SafeVarargs
    static <T> Future<T> passion(final T input, final IxIn in, final BiFunction<T, IxIn, Future<T>>... executors) {
        // Sequence for future management
        Future<T> future = Future.succeededFuture(input);
        for (final BiFunction<T, IxIn, Future<T>> executor : executors) {
            if (Objects.nonNull(executor)) {
                future = future.compose(data -> executor.apply(data, in));
            }
        }
        return future;
    }
}
