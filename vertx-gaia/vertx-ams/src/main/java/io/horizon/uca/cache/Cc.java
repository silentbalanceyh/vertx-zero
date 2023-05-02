package io.horizon.uca.cache;

import io.horizon.eon.em.uca.CcMode;
import io.horizon.exception.internal.CcModeNullException;
import io.horizon.util.HaS;
import io.vertx.core.Future;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * 内存中的缓存架构
 * -- CC 命名：全局模式的缓存
 * -- CC_A 命名：异步模式缓存
 * -- CCT 命名：线程模式缓存
 * -- CCT_A 命名：异步线程模式缓存
 * 暂定四种模式，后续扩展
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Cc<K, V> {

    static <K, V> V pool(final ConcurrentMap<K, V> input, final K key, final Supplier<V> supplier) {
        return HaS.pool(input, key, supplier);
    }


    static <V> V pool(final ConcurrentMap<String, V> input, final Supplier<V> supplier) {
        return HaS.poolThread(input, supplier);
    }

    static <V> Cc<String, V> openThread() {
        return open(CcMode.THREAD);
    }

    static <K, V> Cc<K, V> open() {
        return open(CcMode.STANDARD);
    }

    static <K, V> Cc<K, Future<V>> openA() {
        return new CcAsync<>(CcMode.STANDARD);
    }

    static <K, V> Cc<K, Future<V>> openThreadA() {
        return new CcAsync<>(CcMode.THREAD);
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Cc<K, V> open(final CcMode mode) {
        /*
         * Here are three default implementation of Cc, create new Cc reference and
         * here are all situations for different usage of cache
         * 1) When the Cc created, it is often called in STATIC context
         * 2) Each Cc has three method for:
         * -  pick(Supplier)            -> Singleton or Thread Singleton
         * -  pick(Supplier, K)         -> Pooled by K
         * -  pick(Supplier, Class<?>)  -> Pooled by Class<?> ( named often )
         *
         * Please do not call `Cc.open(CcMode)` in NON-STATIC context because of that
         * it create new Cc in each time and here is no cache of Cc it-self
         *
         * This data structure should replace all the `ConcurrentMap` data structure in:
         * - Zero Framework
         * - Zero Extension Framework
         * - Zero Based Application
         */
        if (Objects.isNull(mode)) {
            throw new CcModeNullException(Cc.class);
        }
        return switch (mode) {
            case THREAD ->
                /*
                 * Thread pool
                 * 1) The cache key must be String type
                 * 2) The pooled component is for different threads:
                 *
                 * thread1 = reference1
                 * thread2 = reference2
                 * thread3 = reference3
                 * ......
                 */
                (Cc<K, V>) new CcThread<V>();
            case STANDARD ->
                /*
                 * Standard Hash Map
                 * 1) The cache key is K type
                 * 2) The reference is V type
                 *
                 * k1 = reference1
                 * k2 = reference2
                 * k3 = reference3
                 * ......
                 */
                new CcMemory<>();
        };
    }

    ConcurrentMap<K, V> store();

    V store(K key);

    V pick(Supplier<V> supplier);

    V pick(Supplier<V> supplier, K key);

    boolean isEmpty();
}
