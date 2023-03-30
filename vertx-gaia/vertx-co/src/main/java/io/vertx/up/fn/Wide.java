package io.vertx.up.fn;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Convert exception type
 */
final class Wide {
    private Wide() {
    }

    /*
     * Workflow:
     * -> Input:        T
     * -> Index 0:      T -> Future<T>
     * -> Index 1:      T -> Future<T>
     * -> ...
     * -> Output:       Future<T>
     */
    @SuppressWarnings("all")
    static <T> Future<T> passion(final T input, final List<Function<T, Future<T>>> executors) {
        // Sequence for future management
        Future<T> future = Future.succeededFuture(input);
        for (final Function<T, Future<T>> executor : executors) {
            if (Objects.nonNull(executor)) {
                future = future.compose(executor);
            }
        }
        return future;
    }

    /*
     * Workflow:
     * -> Input:        T
     * -> T -> Future<T>
     *    T -> Future<T>
     *    ...
     * -> Output:       T ( Input )
     */
    @SuppressWarnings("all")
    static <T> Future<T> parallel(final T input, final Set<Function<T, Future<T>>> executors) {
        final List<Future<T>> futures = new ArrayList<>();
        executors.forEach(executor -> futures.add(executor.apply(input)));
        final List<Future> futureList = new ArrayList<>(futures);
        return CompositeFuture.join(futureList).compose(finished -> {
            final List<T> result = new ArrayList<>();
            finished.list().forEach(item -> result.add((T) item));
            return Future.succeededFuture(result);
        }).compose(item -> Future.succeededFuture(input));
    }
}
