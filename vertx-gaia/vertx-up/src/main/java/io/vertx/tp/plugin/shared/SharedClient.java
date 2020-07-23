package io.vertx.tp.plugin.shared;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.up.atom.Kv;

import java.util.Set;

/**
 * Shared client for shared data in vert.x
 */
public interface SharedClient<K, V> {
    /**
     * Create local map from shared data
     */
    static <K, V> SharedClient createShared(final Vertx vertx, final JsonObject config, final String name) {
        return new SharedClientImpl<K, V>(vertx).create(config, name);
    }

    /**
     * Get reference of AsyncMap
     */
    AsyncMap<K, V> fetchAsync();

    /**
     * Get reference of LocalMap
     */
    LocalMap<K, V> fetchSync();

    SharedClient<K, V> switchClient(final String name);

    Kv<K, V> put(K key, V value);

    Kv<K, V> put(K key, V value, int expiredSecs);

    Kv<K, V> remove(K key);

    V get(K key);

    V get(K key, boolean once);

    int size();

    Set<K> keys();

    @Fluent
    SharedClient<K, V> put(K key, V value, Handler<AsyncResult<Kv<K, V>>> handler);

    @Fluent
    SharedClient<K, V> put(K key, V value, int expiredSecs, Handler<AsyncResult<Kv<K, V>>> handler);

    @Fluent
    SharedClient<K, V> remove(K key, Handler<AsyncResult<Kv<K, V>>> handler);

    @Fluent
    SharedClient<K, V> get(K key, Handler<AsyncResult<V>> handler);

    @Fluent
    SharedClient<K, V> get(K key, boolean once, Handler<AsyncResult<V>> handler);

    @Fluent
    SharedClient<K, V> clear(Handler<AsyncResult<Boolean>> handler);

    /*
     * Map count for usage
     */
    @Fluent
    SharedClient<K, V> size(Handler<AsyncResult<Integer>> handler);

    @Fluent
    SharedClient<K, V> keys(Handler<AsyncResult<Set<K>>> handler);
}
