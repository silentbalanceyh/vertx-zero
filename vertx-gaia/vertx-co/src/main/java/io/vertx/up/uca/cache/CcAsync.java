package io.vertx.up.uca.cache;

import io.horizon.eon.em.CcMode;
import io.vertx.core.Future;
import io.vertx.up.exception.web._501NotSupportException;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

class CcAsync<K, V> implements Cc<K, Future<V>> {
    private final Cc<K, V> cc;
    private final CcMode mode;

    @SuppressWarnings("unchecked")
    CcAsync(final CcMode mode){
        this.mode = mode;
        if(CcMode.THREAD == mode){
            cc = (Cc<K, V>) new CcThread<V>();
        }else if(CcMode.STANDARD == mode){
            cc = new CcMemory<>();
        }else{
            throw new _501NotSupportException(getClass());
        }
        Objects.requireNonNull(cc);
    }

    @Override
    public ConcurrentMap<K, Future<V>> store() {
        final ConcurrentMap<K, Future<V>> cdAsync = new ConcurrentHashMap<>();
        final ConcurrentMap<K, V> stored = this.cc.store();
        stored.forEach((k, v) -> cdAsync.put(k, Future.succeededFuture(v)));
        return cdAsync;
    }

    @Override
    public Future<V> store(K key) {
        return Future.succeededFuture(this.cc.store(key));
    }

    @Override
    public Future<V> pick(Supplier<Future<V>> supplier) {
        if (CcMode.STANDARD == this.mode){
            return Future.failedFuture(new _501NotSupportException(getClass()));
        }
        // Thread name pickup from the system
        final V refOr = this.cc.pick(() -> null);
        if(Objects.isNull(refOr)){
            return supplier.get().compose(v -> {
                final V refR = this.cc.pick(() -> v);
                return Future.succeededFuture(refR);
            });
        }else{
            return Future.succeededFuture(refOr);
        }
    }

    @Override
    public Future<V> pick(Supplier<Future<V>> supplier, K key) {
        final V refOr = this.cc.pick(() -> null, key);
        if(Objects.isNull(refOr)){
            return supplier.get().compose(v -> {
                final V refR = this.cc.pick(() -> v, key);
                return Future.succeededFuture(refR);
            });
        }else{
            return Future.succeededFuture(refOr);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.cc.isEmpty();
    }
}
