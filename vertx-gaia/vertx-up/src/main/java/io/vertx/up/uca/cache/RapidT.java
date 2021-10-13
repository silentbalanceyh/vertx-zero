package io.vertx.up.uca.cache;

import io.vertx.core.Future;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Direct Map to Shared
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class RapidT<T> extends AbstractRapid<String, T> {

    RapidT(final String cacheKey, final int expired) {
        super(cacheKey, expired);
    }

    @Override
    public Future<T> cached(final String key, final Supplier<Future<T>> executor) {
        Objects.requireNonNull(key);
        return this.getAndSet(key, executor, this.expired);
    }
}
