package io.vertx.up.uca.cache;

import io.vertx.up.fn.Fn;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class CcOldMemory<K, T> implements CcOld<K, T> {
    /*
     * Two meanings for T
     * 1) -> Configuration Map, in this kind of situation, the T is JsonObject
     * 2) -> Component Cache, in this kind of situation, the T is component ( interface )
     */
    private final ConcurrentMap<K, T> ambiguityMap = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, T> threadMap = new ConcurrentHashMap<>();

    // ==================== Pick Part ========================
    @Override
    public boolean pickIn(final K key) {
        if (Objects.isNull(key)) {
            return false;
        }
        return this.ambiguityMap.containsKey(key);
    }

    @Override
    public ConcurrentMap<K, T> pick() {
        return this.ambiguityMap;
    }

    /*
     * Default Pool
     * key = supplier.get() and stored in ambiguityMap in each CcPool instance
     */
    @Override
    public T pick(final K key, final Supplier<T> supplier) {
        return Fn.pool(this.ambiguityMap, key, supplier);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T pick(final Class<?> key, final Supplier<T> supplier) {
        return Fn.pool(this.ambiguityMap, (K) key.getName(), supplier);
    }

    // ==================== Fiber Part ========================
    @Override
    public T fiber(final Supplier<T> supplier) {
        return Fn.poolThread(this.threadMap, supplier);
    }

    @Override
    public T fiber(final Supplier<T> supplier, final String root) {
        return Fn.poolThread(this.threadMap, supplier, root);
    }

    @Override
    public T fiber(final Supplier<T> supplier, final Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return Fn.poolThread(this.threadMap, supplier, clazz.getName());
    }
}
