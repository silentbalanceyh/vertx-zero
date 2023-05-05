package io.horizon.uca.cache;

import io.horizon.util.HUt;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class CcThread<V> implements Cc<String, V> {
    private final transient ConcurrentMap<String, V> store = new ConcurrentHashMap<>();

    @Override
    public ConcurrentMap<String, V> store() {
        return this.store;
    }

    @Override
    public boolean isEmpty() {
        return this.store.isEmpty();
    }

    @Override
    public V pick(final Supplier<V> supplier) {
        //        final ConcurrentMap<String, V> pool = this.store.data();
        return HUt.poolThread(this.store, supplier);
    }

    @Override
    public V pick(final Supplier<V> supplier, final String key) {
        return HUt.poolThread(this.store, supplier, key);
    }

    @Override
    public V store(final String key) {
        /*
         * 由于此处是缓存，所以每个线程中存储的实例应该是一致的
         * 由于具有一致性，所以查找到任意线程缓存中的值都可以
         */
        return this.store.keySet().stream()
            .filter(field -> field.startsWith(key))
            .findAny().map(this.store::get)
            .orElse(null);
    }

    @Override
    public boolean remove(final String key) {
        final Set<String> keySet = this.store.keySet().stream()
            .filter(field -> field.startsWith(key))
            .collect(Collectors.toSet());
        keySet.forEach(this.store::remove);
        return true;
    }
}
