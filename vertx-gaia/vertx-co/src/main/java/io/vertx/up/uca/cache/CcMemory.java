package io.vertx.up.uca.cache;

import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.fn.Fn;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class CcMemory<K, V> implements Cc<K, V> {
    private final transient Cd<K, V> data = new CdMap<>();

    @Override
    public Cd<K, V> data() {
        return this.data;
    }

    @Override
    public V pick(final Supplier<V> supplier) {
        throw new _501NotSupportException(this.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public V pick(final Supplier<V> supplier, final Class<?> key) {
        final K cacheKey = (K) key.getName();
        return this.pick(supplier, cacheKey);
    }

    @Override
    public V pick(final Supplier<V> supplier, final K key) {
        final ConcurrentMap<K, V> pool = this.data.data();
        return Fn.pool(pool, key, supplier);
    }
}
