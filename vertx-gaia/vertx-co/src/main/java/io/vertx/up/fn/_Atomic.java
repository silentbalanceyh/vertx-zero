package io.vertx.up.fn;

import io.horizon.annotations.HighLevel;
import io.horizon.fn.HFn;
import io.horizon.util.HaS;
import io.vertx.core.Future;
import io.vertx.core.Promise;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author lang : 2023/4/27
 */
class _Atomic extends HFn {
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
        return MakeUp.parallel(input, executors);
    }

    public static <T> Future<T> parallel(final T input, final List<Function<T, Future<T>>> executors) {
        return MakeUp.parallel(input, new HashSet<>(executors));
    }

    @SafeVarargs
    public static <T> Future<T> parallel(final T input, final Function<T, Future<T>>... executors) {
        return MakeUp.parallel(input, new HashSet<>(Arrays.asList(executors)));
    }

    public static <T> Future<T> passion(final T input, final List<Function<T, Future<T>>> executors) {
        return MakeUp.passion(input, executors);
    }

    @SafeVarargs
    public static <T> Future<T> passion(final T input, final Function<T, Future<T>>... executors) {
        return MakeUp.passion(input, Arrays.asList(executors));
    }

    @HighLevel(value = HaS.class)
    public static <K, V> V pool(final ConcurrentMap<K, V> pool, final K key, final Supplier<V> poolFn) {
        return HaS.pool(pool, key, poolFn);
    }

    @HighLevel(value = HaS.class)
    public static <V> V poolThread(final ConcurrentMap<String, V> pool, final Supplier<V> poolFn) {
        return HaS.poolThread(pool, poolFn);
    }

    @HighLevel(value = HaS.class)
    public static <V> V poolThread(final ConcurrentMap<String, V> pool, final Supplier<V> poolFn, final String key) {
        return HaS.poolThread(pool, poolFn, key);
    }

    /**
     * Vert.x 从 3.8 开始支持 Promise 类型，且单节点 Monad 模式全部改成了 Promise 模式，
     * 这两个 pacifier 方法的目的是为了协同 Promise 和 Future 在旧模式下的执行，保证可协同
     * 主要应用于 UxPool / UxJob / UxMongo 中
     *
     * @param consumer Consumer<Promise<T>>
     * @param <T>      T
     *
     * @return Future<T>
     */
    public static <T> Future<T> pack(final Consumer<Promise<T>> consumer) {
        return Then.then(consumer);
    }

    /**
     * Vert.x 从 3.8 开始支持 Promise 类型，且单节点 Monad 模式全部改成了 Promise 模式，
     * 该函数为回调类型的处理，用于处理后期响应，异步节点模式
     *
     * @param result 异步返回结果，通常是 AsyncResult<T>
     * @param future Future<T>
     * @param ex     Throwable
     * @param <T>    T
     */
    public static <T> void pack(final Object result, final Promise<T> future, final Throwable ex) {
        Then.then(result, future, ex);
    }
}
