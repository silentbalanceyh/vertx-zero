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
    private final transient Cd<K, JsonObject> data = new CdMap<>();

    @Override
    public Cd<K, JsonObject> data() {
        return this.data;
    }

    @Override
    public JsonObject pick(final Supplier<JsonObject> supplier) {
        throw new _501NotSupportException(this.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonObject pick(final Supplier<JsonObject> supplier, final Class<?> key) {
        final K cacheKey = (K) key.getName();
        return this.pick(supplier, cacheKey);
    }

    @Override
    public JsonObject pick(final Supplier<JsonObject> supplier, final K key) {
        final ConcurrentMap<K, JsonObject> pool = this.data.data();
        return Fn.pool(pool, key, supplier);
    }
}
