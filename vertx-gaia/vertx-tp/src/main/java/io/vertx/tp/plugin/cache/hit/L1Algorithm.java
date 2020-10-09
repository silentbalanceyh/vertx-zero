package io.vertx.tp.plugin.cache.hit;

import io.vertx.core.json.JsonObject;

import java.util.TreeMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Split data for SINGLE/MULTI
 */
interface L1Algorithm {

    String dataUnique(String type, TreeMap<String, String> treeMap);

    String dataUnique(String type, JsonObject condition);

    /*
     * Phase 2: Cache Data Generation
     */

    /*
     * Phase 3: Cache Data Tree Generation
     */
}
