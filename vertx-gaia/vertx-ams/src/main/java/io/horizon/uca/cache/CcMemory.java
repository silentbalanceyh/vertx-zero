package io.horizon.uca.cache;

import io.horizon.exception.internal.NotImplementException;
import io.horizon.util.HaS;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class CcMemory<K, V> implements Cc<K, V> {
    private final transient ConcurrentMap<K, V> store = new ConcurrentHashMap<>();
    //    private final transient Cd<K, V> store = new CdMap<>();

    @Override
    public ConcurrentMap<K, V> store() {
        return this.store;
    }

    @Override
    public boolean isEmpty() {
        return this.store.isEmpty();
    }

    @Override
    public V pick(final Supplier<V> supplier) {
        throw new NotImplementException(this.getClass());
    }

    @Override
    public V pick(final Supplier<V> supplier, final K key) {
        //        final ConcurrentMap<K, V> pool = this.store.data();
        return HaS.pool(this.store, key, supplier);
    }

    @Override
    public V store(final K key) {
        return this.store.getOrDefault(key, null);
    }
}
