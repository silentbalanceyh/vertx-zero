package io.vertx.up.plugin.cache.l1;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.plugin.cache.hit.CMessage;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
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
     *
     */
    void write(CMessage... message);

    /*
     * L1 Algorithm for deleting data from cache here, but the deleting processing depend on
     * implementation of sub-set here.
     */
    void delete(CMessage... message);

    /*
     * Read data with callback refresh the cache
     * 1. Read data from cache first
     * 2. If hit, returned directly
     * 3. If handler, call on database to get data
     */
    <T> Future<T> readAsync(CMessage message);

    <T> T read(CMessage message);

    /*
     * Exist data without callback refresh the cache
     * 1. Read data from cache first
     * 2. If hit, returned directly
     * 3. If handler, call on database to get data
     */
    Future<Boolean> existAsync(CMessage message);

    Boolean exist(CMessage message);
}
