package io.vertx.up.fn;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.up.exception.ZeroRunException;
import io.vertx.up.exception.heart.PoolKeyNullException;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Convert exception type
 */
final class Deliver {
    private Deliver() {
    }

    /**
     * @param supplier T supplier function
     * @param runCls   ZeroRunException definition
     * @param <T>      T type of object
     *
     * @return Final T or throw our exception
     */
    static <T> T execRun(final Supplier<T> supplier, final Class<? extends ZeroRunException> runCls, final Object... args) {
        T ret = null;
        try {
            ret = supplier.get();
        } catch (final Throwable ex) {
            final Object[] argument = Ut.elementAdd(args, ex);
            final ZeroRunException error = Ut.instance(runCls, argument);
            if (null != error) {
                throw error;
            }
        }
        return ret;
    }

    /**
     * Memory cache pool implemented by ConcurrentMap( k = v ) instead of create new each time
     *
     * @param pool   Memory concurrent hash map
     * @param key    Input key for cache
     * @param poolFn Supplier of value when create new ( If not in cache )
     * @param <K>    key type
     * @param <V>    value type
     *
     * @return Get or Created V for value
     */
    static <K, V> V exec(final ConcurrentMap<K, V> pool, final K key, final Supplier<V> poolFn) {
        if (Objects.isNull(key)) {
            throw new PoolKeyNullException();
        }
        V reference = pool.get(key);
        if (null == reference) {
            reference = poolFn.get();
            if (null != reference) {
                pool.put(key, reference);
            }
        }
        return reference;
    }

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
