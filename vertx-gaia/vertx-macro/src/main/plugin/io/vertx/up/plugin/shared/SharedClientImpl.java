package io.vertx.up.plugin.shared;

import io.horizon.atom.common.Kv;
import io.horizon.exception.WebException;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import io.vertx.up.exception.web._500SharedDataModeException;
import io.vertx.up.fn.Fn;

import java.util.Objects;
import java.util.Set;

@SuppressWarnings("all")
public class SharedClientImpl<K, V> implements SharedClient<K, V> {

    private static final Annal LOGGER = Annal.get(SharedClientImpl.class);

    private static final Cc<String, SharedClient> CC_CLIENTS = Cc.open();

    private final transient Vertx vertx;
    private transient LocalMap<K, V> syncMap;
    private transient AsyncMap<K, V> asyncMap;
    private transient String poolName;

    SharedClientImpl(final Vertx vertx, final String name) {
        this.vertx = vertx;
        this.poolName = name;
    }

    public static <K, V> SharedClient<K, V> create(final Vertx vertx, final String name) {
        return CC_CLIENTS.pick(() -> new SharedClientImpl(vertx, name), name);
        // return Fn.po?l(CLIENTS, name, () -> new SharedClientImpl(vertx, name));
    }

    private void async(final Handler<AsyncResult<AsyncMap<K, V>>> handler) {
        if (Objects.isNull(this.asyncMap)) {
            final SharedData sd = this.vertx.sharedData();
            // Async map created
            LOGGER.info(INFO.INFO_ASYNC_START);
            sd.<K, V>getAsyncMap(this.poolName, res -> {
                if (res.succeeded()) {
                    this.asyncMap = res.result();
                    LOGGER.info(INFO.INFO_ASYNC_END, String.valueOf(this.asyncMap.hashCode()), this.poolName);
                    handler.handle(Future.succeededFuture(res.result()));
                } else {
                    final WebException error = new _500SharedDataModeException(getClass(), res.cause());
                    handler.handle(Future.failedFuture(error));
                }
            });
        } else {
            handler.handle(Future.succeededFuture(this.asyncMap));
        }
    }

    private LocalMap<K, V> sync() {
        if (Objects.isNull(this.syncMap)) {
            final SharedData sd = this.vertx.sharedData();
            // Sync map created
            this.syncMap = sd.getLocalMap(this.poolName);
            LOGGER.info(INFO.INFO_SYNC, String.valueOf(this.syncMap.hashCode()), this.poolName);
        }
        return this.syncMap;
    }

    @Override
    public SharedClient<K, V> switchClient(final String name) {
        return SharedClientImpl.create(this.vertx, name);
    }

    @Override
    public Kv<K, V> put(final K key, final V value) {
        final V reference = this.sync().get(key);
        // Add & Replace
        Fn.runAt(null == reference, LOGGER,
            () -> this.sync().put(key, value),
            () -> this.sync().replace(key, value));
        return Kv.create(key, value);
    }

    @Override
    public Kv<K, V> put(final K key, final V value, final int seconds) {
        Kv<K, V> result = this.put(key, value);
        LOGGER.info(INFO.INFO_TIMER_PUT, String.valueOf(key), String.valueOf(seconds));
        this.vertx.setTimer(seconds * 1000, id -> {
            final V existing = this.get(key);
            if (Objects.nonNull(existing)) {
                LOGGER.info(INFO.INFO_TIMER_EXPIRE, String.valueOf(key));
                this.remove(key);
            } else {
                LOGGER.info(INFO.INFO_TIMER_REMOVED, String.valueOf(key));
            }
        });
        return result;
    }

    @Override
    public SharedClient<K, V> put(final K key, final V value,
                                  final Handler<AsyncResult<Kv<K, V>>> handler) {
        this.async(map -> map.result().get(key, res -> {
            if (res.succeeded()) {
                final V reference = res.result();
                Fn.runAt(null == reference, LOGGER,
                    // Successed for Add
                    () -> map.result()
                        .put(key, value, added -> this.putHandler(added, key, value, handler)),
                    // Successed for Replace
                    () -> map.result()
                        .replace(key, value, replaced -> this.putHandler(replaced, key, value, handler)));
            } else {
                final WebException error = new _500SharedDataModeException(getClass(), res.cause());
                handler.handle(Future.failedFuture(error));
            }
        }));
        return this;
    }

    @Override
    public SharedClient<K, V> put(final K key, final V value, final int seconds,
                                  final Handler<AsyncResult<Kv<K, V>>> handler) {
        LOGGER.info(INFO.INFO_TIMER_PUT, String.valueOf(key), String.valueOf(seconds));
        final Integer ms = seconds * 1000;
        this.async(map -> map.result().get(key, res -> {
            if (res.succeeded()) {
                final V reference = res.result();
                Fn.runAt(null == reference, LOGGER,
                    // Successed for Add
                    () -> map.result()
                        .put(key, value, ms, added -> this.putHandler(added, key, value, handler)),
                    // Successed for Replace
                    () -> map.result()
                        .replace(key, value, ms, replaced -> this.putHandler(replaced, key, value, handler)));
            } else {
                final WebException error = new _500SharedDataModeException(getClass(), res.cause());
                handler.handle(Future.failedFuture(error));
            }
        }));
        return this;
    }

    private <K, V> void putHandler(final AsyncResult done, final K key, final V value,
                                   final Handler<AsyncResult<Kv<K, V>>> handler) {
        if (done.succeeded()) {
            LOGGER.info(INFO.INFO_TIMER_EXPIRE, key);
            handler.handle(Future.succeededFuture(Kv.create(key, value)));
        } else {
            final WebException error = new _500SharedDataModeException(getClass(), done.cause());
            handler.handle(Future.failedFuture(error));
        }
    }

    @Override
    public Kv<K, V> remove(final K key) {
        final V removed = this.sync().remove(key);
        return Kv.create(key, removed);
    }

    @Override
    public V get(final K key) {
        return this.sync().get(key);
    }

    @Override
    public boolean clear() {
        this.sync().clear();
        return true;
    }

    @Override
    public V get(final K key, final boolean once) {
        final V value = this.get(key);
        if (once) {
            this.remove(key);
        }
        return value;
    }

    @Override
    public SharedClient<K, V> remove(final K key,
                                     final Handler<AsyncResult<Kv<K, V>>> handler) {
        this.async(map -> map.result().remove(key, res -> {
            if (res.succeeded()) {
                final V reference = res.result();
                handler.handle(Future.succeededFuture(Kv.create(key, reference)));
            } else {
                final WebException error = new _500SharedDataModeException(getClass(), res.cause());
                handler.handle(Future.failedFuture(error));
            }
        }));
        return this;
    }

    @Override
    public SharedClient<K, V> get(final K key,
                                  final Handler<AsyncResult<V>> handler) {
        this.async(map -> map.result().get(key, handler));
        return this;
    }

    @Override
    public SharedClient<K, V> get(K key, boolean once,
                                  Handler<AsyncResult<V>> handler) {
        final SharedClient<K, V> reference = this.get(key, handler);
        if (once) {
            this.async(map -> map.result().remove(key, handler));
        }
        return reference;
    }

    @Override
    public SharedClient<K, V> clear(Handler<AsyncResult<Boolean>> handler) {
        this.async(map -> map.result().clear(result -> handler.handle(Future.succeededFuture(Boolean.TRUE))));
        return this;
    }

    /*
     * Shared Enhancement for
     *
     * 1) Session Management
     * 2) Cache Management
     * 3) Login/Logout Management
     */
    @Override
    public SharedClient<K, V> size(Handler<AsyncResult<Integer>> handler) {
        this.async(map -> map.result().size(handler));
        return this;
    }

    @Override
    public SharedClient<K, V> keys(Handler<AsyncResult<Set<K>>> handler) {
        this.async(map -> map.result().keys(handler));
        return this;
    }

    @Override
    public int size() {
        return this.sync().size();
    }

    @Override
    public Set<K> keys() {
        return this.sync().keySet();
    }
}
