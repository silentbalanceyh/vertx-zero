package io.horizon.uca.cache;

import io.horizon.eon.em.uca.CcMode;
import io.horizon.exception.internal.OperationException;
import io.vertx.core.Future;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

class CcAsync<K, V> implements Cc<K, Future<V>> {
    private final Cc<K, V> cc;
    private final CcMode mode;

    @SuppressWarnings("unchecked")
    CcAsync(final CcMode mode) {
        this.mode = mode;
        if (CcMode.THREAD == mode) {
            this.cc = (Cc<K, V>) new CcThread<V>();
        } else if (CcMode.STANDARD == mode) {
            this.cc = new CcMemory<>();
        } else {
            throw new OperationException(this.getClass(), "Constructor(CcMode)");
        }
        Objects.requireNonNull(this.cc);
    }

    @Override
    public ConcurrentMap<K, Future<V>> store() {
        final ConcurrentMap<K, Future<V>> cdAsync = new ConcurrentHashMap<>();
        final ConcurrentMap<K, V> stored = this.cc.store();
        stored.forEach((k, v) -> cdAsync.put(k, Future.succeededFuture(v)));
        return cdAsync;
    }

    @Override
    public Future<V> store(final K key) {
        return Future.succeededFuture(this.cc.store(key));
    }

    @Override
    public Future<V> pick(final Supplier<Future<V>> supplier) {
        if (CcMode.STANDARD == this.mode) {
            return Future.failedFuture(new OperationException(this.getClass(), "pick(Supplier)"));
        }
        // Thread name pickup from the system
        final V refOr = this.cc.pick(() -> null);
        if (Objects.isNull(refOr)) {
            return supplier.get().compose(v -> {
                final V refR = this.cc.pick(() -> v);
                return Future.succeededFuture(refR);
            });
        } else {
            return Future.succeededFuture(refOr);
        }
    }

    @Override
    public Future<V> pick(final Supplier<Future<V>> supplier, final K key) {
        final V refOr = this.cc.pick(() -> null, key);
        if (Objects.isNull(refOr)) {
            return supplier.get().compose(v -> {
                final V refR = this.cc.pick(() -> v, key);
                return Future.succeededFuture(refR);
            });
        } else {
            return Future.succeededFuture(refOr);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.cc.isEmpty();
    }
}
