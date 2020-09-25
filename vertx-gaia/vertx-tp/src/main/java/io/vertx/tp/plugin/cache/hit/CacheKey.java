package io.vertx.tp.plugin.cache.hit;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Calculated cache key based on L1Config etc
 */
public interface CacheKey {
    /*
     * Matrix store
     */
    String unique(CacheMeta meta);
}
