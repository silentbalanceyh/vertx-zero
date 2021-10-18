package io.vertx.up.uca.cache;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.up.unity.Ux;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RapidUser<T> extends AbstractRapid<String, T> {
    private final transient String rootKey;

    RapidUser(final User user, final String rootKey) {
        super(user);
        this.rootKey = rootKey;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Future<T> cached(final String key, final Supplier<Future<T>> executor) {
        Objects.requireNonNull(key);
        return this.pool.<String, JsonObject>get(this.rootKey).compose(cached -> {
            if (Objects.isNull(cached)) {
                cached = new JsonObject();
            }
            if (cached.containsKey(key)) {
                this.logger().info("[ Cache ] \u001b[0;37mK = `{2}`, R = `{1}`, P = `{0}`\u001b[m",
                    this.pool.name(), this.rootKey, key);
                return Ux.future((T) cached.getValue(key));
            } else {
                final JsonObject stored = cached;
                return executor.get().compose(item -> {
                    stored.put(key, item);
                    return this.pool.put(this.rootKey, stored).compose(nil -> Ux.future(item));
                });
            }
        });
    }
}
