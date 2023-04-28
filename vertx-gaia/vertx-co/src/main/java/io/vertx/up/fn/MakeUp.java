package io.vertx.up.fn;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * @author lang : 2023/4/27
 */
final class MakeUp {
    private MakeUp() {
    }

    /*
     * 串行编排器，Workflow:
     * -> Input:        T
     * -> Index 0:      T -> Future<T>
     * -> Index 1:      T -> Future<T>
     * -> ...
     * -> Output:       Future<T>
     * 整体流程：input -> executor1 -> executor2 -> output
     * 执行流程中函数输入输出不发生任何变化，都是 T
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
}
