package io.vertx.up.fn;

import io.horizon.annotations.HLinking;
import io.horizon.util.HaS;
import io.vertx.core.Future;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author lang : 2023/4/27
 */
class _Atomic {
    protected _Atomic() {
    }

    // ---------------------------------------------------- 原子函数 ----------------------------------------------------
    /*
     * 全部 p 前缀的原子函数
     * - parallel: 并行编排
     * - passion:  串行编排
     * - pool:     池化（缓存函数）
     */
    public static <T> Future<T> parallel(final T input, final Set<Function<T, Future<T>>> executors) {
        return Atomic.parallel(input, executors);
    }

    public static <T> Future<T> parallel(final T input, final List<Function<T, Future<T>>> executors) {
        return Atomic.parallel(input, new HashSet<>(executors));
    }

    public static <T> Future<T> parallel(final T input, final Function<T, Future<T>>... executors) {
        return Atomic.parallel(input, new HashSet<>(Arrays.asList(executors)));
    }

    public static <T> Future<T> passion(final T input, final List<Function<T, Future<T>>> executors) {
        return Atomic.passion(input, executors);
    }

    public static <T> Future<T> passion(final T input, final Function<T, Future<T>>... executors) {
        return Atomic.passion(input, Arrays.asList(executors));
    }

    @HLinking(value = HaS.class)
    public static <K, V> V pool(final ConcurrentMap<K, V> pool, final K key, final Supplier<V> poolFn) {
        return HaS.pool(pool, key, poolFn);
    }

    @HLinking(value = HaS.class)
    public static <V> V poolThread(final ConcurrentMap<String, V> pool, final Supplier<V> poolFn) {
        return HaS.poolThread(pool, poolFn);
    }

    @HLinking(value = HaS.class)
    public static <V> V poolThread(final ConcurrentMap<String, V> pool, final Supplier<V> poolFn, final String key) {
        return HaS.poolThread(pool, poolFn, key);
    }
}
