package io.vertx.up.uca.cache;

import io.vertx.up.fn.Fn;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class CcMemory<K, T> implements Cc<K, T> {
    private final ConcurrentMap<K, T> instanceMap = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, T> threadMap = new ConcurrentHashMap<>();

    /*
     * Default Pool
     * key = supplier.get() and stored in instanceMap in each CcPool instance
     */
    @Override
    public T pick(final K key, final Supplier<T> supplier) {
        return Fn.pool(this.instanceMap, key, supplier);
    }

    @Override
    public boolean is(final K key) {
        if (Objects.isNull(key)) {
            return false;
        }
        return this.instanceMap.containsKey(key);
    }

    @Override
    public T pick(final Supplier<T> supplier) {
        return Fn.poolThread(this.threadMap, supplier);
    }

    @Override
    public T pick(final Supplier<T> supplier, final String root) {
        return Fn.poolThread(this.threadMap, supplier, root);
    }

    @Override
    public T pick(final Supplier<T> supplier, final Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return Fn.poolThread(this.threadMap, supplier, clazz.getName());
    }
}
