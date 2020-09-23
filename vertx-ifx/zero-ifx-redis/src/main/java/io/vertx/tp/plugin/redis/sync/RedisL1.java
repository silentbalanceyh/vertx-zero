package io.vertx.tp.plugin.redis.sync;

import io.vertx.core.Future;
import io.vertx.tp.plugin.cache.HKey;
import io.vertx.tp.plugin.cache.l1.AbstractL1;
import io.vertx.up.eon.em.ChangeFlag;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class RedisL1 extends AbstractL1 {

    @Override
    public <T> Future<T> flushAsync(final T input, final ChangeFlag type) {
        return null;
    }

    @Override
    public <T> T flush(final T input, final ChangeFlag type) {
        return null;
    }

    @Override
    public <T> Future<T> hitAsync(final HKey key) {
        return null;
    }

    @Override
    public <T> T hit(final HKey key) {
        return null;
    }
}
