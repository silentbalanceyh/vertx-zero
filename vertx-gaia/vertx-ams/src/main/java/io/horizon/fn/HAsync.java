package io.horizon.fn;

import io.horizon.exception.AbstractException;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

import java.util.*;
import java.util.function.Function;

/**
 * @author lang : 2023-05-27
 */
class HAsync {
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
     * 并行编排器，Workflow，返回值为输入
     * -> Input:        T
     * -> T -> Future<T>
     *    T -> Future<T>
     *    ...
     * -> Output:       T ( Input )
     * 整体流程：input -> executor1  ->  output
     *                -> executor2
     *
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

    @SuppressWarnings("all")
    static <T, E extends AbstractException> Future<T> pass(
        final T response, final E error,
        final Function<Collection<Boolean>, Boolean> checkFn,
        final Set<Function<T, Future<Boolean>>> executors) {
        final List<Future> matched = new ArrayList<>();
        executors.forEach(executor -> matched.add(executor.apply(response)));
        return CompositeFuture.join(matched).compose(finished -> {
            final List<Boolean> result = new ArrayList<>();
            finished.list().forEach(item -> result.add((Boolean) item));
            final boolean checked = checkFn.apply(result);
            if (checked) {
                return Future.succeededFuture(response);
            } else {
                return Future.failedFuture(error);
            }
        });
    }
}
