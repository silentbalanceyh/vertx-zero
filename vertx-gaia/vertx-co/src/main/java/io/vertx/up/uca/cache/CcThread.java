package io.vertx.up.uca.cache;

import io.vertx.up.fn.Fn;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class CcThread<V> implements Cc<String, V> {
    private final transient Cd<String, V> data = new CdMap<>();

    @Override
    public Cd<String, V> data() {
        return this.data;
    }

    @Override
    public V pick(final Supplier<V> supplier) {
        final ConcurrentMap<String, V> pool = this.data.data();
        return Fn.poolThread(pool, supplier);
    }

    @Override
    public V pick(final Supplier<V> supplier, final Class<?> key) {
        final String cacheKey = key.getName();
        return this.pick(supplier, cacheKey);
    }

    @Override
    public V pick(final Supplier<V> supplier, final String key) {
        final ConcurrentMap<String, V> pool = this.data.data();
        return Fn.poolThread(pool, supplier, key);
    }
}
