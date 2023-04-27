package io.horizon.util;

import io.horizon.eon.VString;
import io.horizon.exception.internal.PoolNullException;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author lang : 2023/4/27
 */
final class HPool {
    private HPool() {
    }

    static <V> V poolThread(final Map<String, V> pool, final Supplier<V> poolFn, final String key) {
        final String threadName = Thread.currentThread().getName();
        final String keyPool;
        if (HString.isEmpty(key)) {
            keyPool = threadName;
        } else {
            keyPool = key + VString.SLASH + threadName;
        }
        return pool(pool, keyPool, poolFn);
    }

    static <K, V> V pool(final Map<K, V> pool, final K key, final Supplier<V> poolFn) {
        if (Objects.isNull(pool)) {
            // ERR-10004, 缓存传入 pool 不可为空
            throw new PoolNullException(HPool.class);
        }
        return pool.computeIfAbsent(key, k -> poolFn.get());
    }
}
