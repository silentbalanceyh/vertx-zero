package io.vertx.up.uca.cache;

import io.vertx.core.Future;
import io.vertx.up.atom.Kv;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.UxPool;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AbstractRapid<K, T> implements Rapid<K, T> {
    protected final transient UxPool pool;
    protected final transient int expired;

    protected AbstractRapid(final String poolName, final int expired) {
        this.pool = Ux.Pool.on(poolName);
        this.expired = expired;
    }

    protected Future<T> getAndSet(final K key, final Supplier<Future<T>> executor, final int expired) {
        return this.pool.<K, T>get(key).compose(queried -> {
            if (Objects.isNull(queried)) {
                return executor.get()
                    .compose(actual -> 0 < expired ?
                        this.pool.put(key, actual) :
                        this.pool.put(key, actual, expired)
                    )
                    .compose(Kv::value);
            } else {
                this.logger().info("[ Cache ] Hit {0} by {1}", this.pool.name(), key);
                return Ux.future(queried);
            }
        });
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    @Override
    public Future<T> write(final K key, final T value, final int expired) {
        if (0 < expired) {
            return this.pool.put(key, value, expired).compose(Kv::value);
        } else {
            return this.pool.put(key, value).compose(Kv::value);
        }
    }
}
