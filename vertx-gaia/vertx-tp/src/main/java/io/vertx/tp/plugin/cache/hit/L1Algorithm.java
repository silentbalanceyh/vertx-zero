package io.vertx.tp.plugin.cache.hit;

import io.vertx.core.json.JsonObject;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Split data for SINGLE/MULTI
 */
public interface L1Algorithm {
    /*
     * Common usage to generate cache key
     * 1) treeMap is native method in lower
     * 2) condition is high level method for data unique
     * This api is called by `CMessage` object purely
     */
    String dataUnique(String type, TreeMap<String, String> treeMap);

    String dataUnique(String type, JsonObject condition);

    /*
     * Phase 2: Cache Data Generation
     */
    ConcurrentMap<String, Object> dataCache(JsonObject jsonBody);

    /*
     * Phase 3: Cache Data Tree Generation
     */
    ConcurrentMap<String, Object> dataKey(JsonObject jsonBody);
}
