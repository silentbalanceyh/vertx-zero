package io.horizon.uca.cache;

import io.horizon.eon.em.CcMode;
import io.horizon.exception.internal.CcModeNullException;
import io.horizon.util.HaS;
import io.vertx.core.Future;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * Cache Interface for three usage:
 *
 * - Component Cache
 * - Config Cache
 * - Thread Component Cache
 *
 * It's new structure for different cache stored instead of single one
 *
 * 1) Here are Cd data structure to control internal storage.
 * 2) The default implementation class is `CdMem` ( Default Cd )
 *
 * Cc: Component Cache / Config Cache
 * Cd: Component Data / Cache Data
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
