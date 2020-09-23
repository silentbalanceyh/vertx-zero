package io.vertx.tp.plugin.cache.hit;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Calculated cache key based on L1Config etc
 */
public interface HKey {
    /*
     * Matrix store
     */
    String unique(HMeta meta);
}
