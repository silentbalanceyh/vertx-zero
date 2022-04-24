package io.vertx.up.uca.cache;

import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.fn.Fn;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class CcConfig<K> implements Cc<K, JsonObject> {
    private final transient Cd<K, JsonObject> store = new CdMap<>();

    @Override
    public Cd<K, JsonObject> store() {
        return this.store;
    }

    @Override
    public JsonObject pick(final Supplier<JsonObject> supplier) {
        throw new _501NotSupportException(this.getClass());
    }

    @Override
    public JsonObject pick(final Supplier<JsonObject> supplier, final K key) {
        final ConcurrentMap<K, JsonObject> pool = this.store.data();
        return Fn.pool(pool, key, supplier);
    }

    @Override
    public JsonObject store(final K key) {
        return this.store.data(key);
    }
}
