package io.vertx.up.unity;

import io.vertx.core.Future;
import io.vertx.tp.plugin.shared.MapInfix;
import io.vertx.tp.plugin.shared.SharedClient;
import io.vertx.up.atom.Kv;
import io.vertx.up.exception.web._500PoolInternalException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Shared Data for pool usage in utility X
 */
@SuppressWarnings("all")
public class UxPool {
    private static final Annal LOGGER = Annal.get(UxPool.class);
    private transient final String name;
    private transient final SharedClient client;

    UxPool() {
        this.name = MapInfix.getDefaultName();
        this.client = MapInfix.getClient();
    }

    UxPool(final String name) {
        this.name = name;
        this.client = MapInfix.getClient().switchClient(name);
    }

    // Put Operation
    public <K, V> Future<Kv<K, V>> put(final K key, final V value) {
        return Fn.<Kv<K, V>>thenGeneric(future -> this.client.put(key, value, res -> {
            LOGGER.debug(Info.POOL_PUT, key, value, this.name);
            Fn.thenGeneric(res, future, To.toError(_500PoolInternalException.class, this.getClass(), this.name, "put"));
        }));
    }

    public <K, V> Future<Kv<K, V>> put(final K key, final V value, int expiredSecs) {
        return Fn.<Kv<K, V>>thenGeneric(future -> this.client.<K, V>put(key, value, expiredSecs, res -> {
            LOGGER.debug(Info.POOL_PUT_TIMER, key, value, this.name, String.valueOf(expiredSecs));
            Fn.thenGeneric(res, future, To.toError(_500PoolInternalException.class, this.getClass(), this.name, "put"));
        }));
    }

    // Remove
    public <K, V> Future<Kv<K, V>> remove(final K key) {
        return Fn.<Kv<K, V>>thenGeneric(future -> this.client.<K, V>remove(key, res -> {
            LOGGER.debug(Info.POOL_REMOVE, key, this.name);
            Fn.thenGeneric(res, future, To.toError(_500PoolInternalException.class, this.getClass(), this.name, "remove"));
        }));
    }

    // Get
    public <K, V> Future<V> get(final K key) {
        return Fn.<V>thenGeneric(future -> this.client.get(key, res -> {
            LOGGER.debug(Info.POOL_GET, key, this.name, false);
            Fn.thenGeneric(res, future, To.toError(_500PoolInternalException.class, this.getClass(), this.name, "get"));
        }));
    }

    public <K, V> Future<ConcurrentMap<K, V>> get(final Set<K> keys) {
        final ConcurrentMap<K, Future<V>> futureMap = new ConcurrentHashMap<>();
        keys.forEach(key -> futureMap.put(key, this.get(key)));
        return Combine.thenCombine(futureMap);
    }

    public <K, V> Future<V> get(final K key, final boolean once) {
        return Fn.<V>thenGeneric(future -> this.client.get(key, once, res -> {
            LOGGER.debug(Info.POOL_GET, key, this.name, once);
            Fn.thenGeneric(res, future, To.toError(_500PoolInternalException.class, this.getClass(), this.name, "get"));
        }));
    }

    public Future<Boolean> clear() {
        return Fn.<Boolean>thenGeneric(future -> this.client.clear(res -> {
            LOGGER.debug(Info.POOL_CLEAR, this.name);
            Fn.thenGeneric(res, future, To.toError(_500PoolInternalException.class, this.getClass(), this.name, "clear"));
        }));
    }

    // Count
    public Future<Integer> size() {
        return Fn.<Integer>thenGeneric(future -> this.client.size(res -> {
            Fn.thenGeneric(res, future, To.toError(_500PoolInternalException.class, this.getClass(), this.name, "size"));
        }));
    }

    public Future<Set<String>> keys() {
        return Fn.<Set<String>>thenGeneric(future -> this.client.keys(res -> {
            Fn.thenGeneric(res, future, To.toError(_500PoolInternalException.class, this.getClass(), this.name, "keys"));
        }));
    }
}
