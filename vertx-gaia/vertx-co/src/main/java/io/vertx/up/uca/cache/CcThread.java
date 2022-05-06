package io.vertx.up.uca.cache;

import io.vertx.up.fn.Fn;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class CcThread<V> implements Cc<String, V> {
    private final transient Cd<String, V> store = new CdMap<>();

    @Override
    public Cd<String, V> store() {
        return this.store;
    }

    @Override
    public V pick(final Supplier<V> supplier) {
        final ConcurrentMap<String, V> pool = this.store.data();
        return Fn.poolThread(pool, supplier);
    }

    @Override
    public V pick(final Supplier<V> supplier, final String key) {
        final ConcurrentMap<String, V> pool = this.store.data();
        return Fn.poolThread(pool, supplier, key);
    }

    @Override
    public V store(final String key) {
        return this.store.data(key);
    }
}
