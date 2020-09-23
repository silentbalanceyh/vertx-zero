package io.vertx.tp.plugin.cache.l1;

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
public interface DbCache {
}
