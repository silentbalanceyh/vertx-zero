package io.vertx.tp.plugin.cache.hit;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Calculated cache key based on L1Config etc
 */
public interface CacheKey {
    /*
     * Bind Part for metadata
     * 1. primary keys
     * 2. condition
     * 3. type
     */

    /*
     * Matrix store
     */
    String unique(CacheMeta meta);

    /*
     * Where it's primary node
     */
    boolean primary();
}
