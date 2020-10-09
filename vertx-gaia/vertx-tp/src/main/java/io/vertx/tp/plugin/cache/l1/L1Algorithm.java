package io.vertx.tp.plugin.cache.l1;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.hit.CacheMeta;
import io.vertx.up.eon.em.ChangeFlag;

import java.util.TreeMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Split data for SINGLE/MULTI
 */
public interface L1Algorithm {
    /*
     * Phase 1: Data Delivery
     */
    <T> Buffer dataDelivery(T entity, ChangeFlag flag, CacheMeta meta);

    /*
     * Phase 2: Cache Data Generation
     */

    /*
     * Phase 3: Cache Data Tree Generation
     */

    /*
     * Phase 4: Unique String Generation
     * 1) dataUnique(String,TreeMap<String,String>), DATA:????
     */
    String dataUnique(String type, TreeMap<String, String> treeMap);

    String dataUnique(String type, JsonObject condition);
}
