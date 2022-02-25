package io.vertx.up.uca.cache;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.UxPool;
import io.vertx.up.util.Ut;

import java.util.Objects;

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

    protected AbstractRapid(final User user) {
        Objects.requireNonNull(user);
        this.expired = -1;
        final JsonObject credit = Ut.valueJObject(user.principal());
        final String poolName = credit.getString(KName.HABITUS);
        this.pool = Ux.Pool.on(poolName);
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    @Override
    public Future<T> write(final K key, final T value) {
        if (0 < this.expired) {
            return this.pool.put(key, value, this.expired).compose(Kv::value);
        } else {
            return this.pool.put(key, value).compose(Kv::value);
        }
    }

    @Override
    public Future<T> clear(final K key) {
        return this.pool.<K, T>remove(key).compose(Kv::value);
    }

    @Override
    public Future<T> read(final K key) {
        return this.pool.get(key);
    }
}
