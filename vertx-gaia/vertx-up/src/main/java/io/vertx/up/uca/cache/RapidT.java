package io.vertx.up.uca.cache;

import io.vertx.core.Future;
import io.vertx.up.unity.Ux;

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
        return this.pool.<String, T>get(key).compose(queried -> {
            if (Objects.isNull(queried)) {
                return executor.get()
                    .compose(actual -> 0 < this.expired ?
                        this.pool.put(key, actual, this.expired) :
                        this.pool.put(key, actual)
                    )
                    .compose(kv -> Ux.future(kv.value()));
            } else {
                this.logger().info("[ Cache ] \u001b[0;37mK = `{1}`, P = `{0}`\u001b[m", this.pool.name(), key);
                return Ux.future(queried);
            }
        });
    }
}
