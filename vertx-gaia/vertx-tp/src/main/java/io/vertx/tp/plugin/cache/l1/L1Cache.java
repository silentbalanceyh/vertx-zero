package io.vertx.tp.plugin.cache.l1;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.tp.plugin.cache.hit.HKey;
import io.vertx.tp.plugin.cache.hit.HMeta;
import io.vertx.up.eon.em.ChangeFlag;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * The first Level for cache, this interface is used by Jooq
 * between `Dao` and `Database`, here are situation in current version:
 *
 * 1) Enabled cache between `Dao` and `Database`
 * 2) Provide default arithmetic for cache key calculation
 * 3) Three fixed situations:
 * -- 3.1）The cache key is primary key directly
 * -- 3.2）The cache key is unique key directly
 * -- 3.3）The cache key is condition for `existing/missing` checking
 */
public interface L1Cache {

    L1Cache bind(L1Config config);

    L1Cache bind(Vertx vertx);

    /*
     * Policy 1:
     *
     * Double check for write / read
     *
     * write include:
     * - ADD
     * - DELETE
     * - UPDATE
     * read include:
     * - by key
     * - by field
     * - by unique condition
     */
    <T> void flushAsync(T input, ChangeFlag type, HMeta meta);

    <T> Future<T> hitAsync(HKey key, HMeta meta);

    <T> T hit(HKey key, HMeta meta);
}
