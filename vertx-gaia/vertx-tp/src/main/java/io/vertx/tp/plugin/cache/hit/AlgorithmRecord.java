package io.vertx.tp.plugin.cache.hit;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class AlgorithmRecord extends AbstractL1Algorithm {

    @Override
    public String dataType() {
        return "RECORD";
    }

    @Override
    public void dataProcess(final ConcurrentMap<String, Object> resultMap, final JsonObject jsonBody, final boolean isRefer) {
        final Object dataPart = jsonBody.getValue("data");
        if (dataPart instanceof JsonObject) {
            /*
             * DATA
             */
            final String cacheKey = this.calculateKey(jsonBody);
            /*
             * Cache Communication
             */
            resultMap.put(cacheKey, dataPart);           // Cache Write
        }
    }

    @Override
    public void dataRefer(final ConcurrentMap<String, Object> resultMap, final JsonObject jsonBody) {
        final JsonObject condition = jsonBody.getJsonObject("condition");
        if (Ut.notNil(condition)) {
            /*
             * Tree for cache
             */
            final String cacheKey = this.calculateKey(jsonBody);
            /*
             * Condition building
             */
            final String revertKey = this.dataUnique(jsonBody.getString("type"), condition);
            resultMap.put(revertKey, cacheKey);
        }
    }

    @Override
    public void dataTree(final ConcurrentMap<String, Object> resultMap, final JsonObject jsonBody) {

    }

    private String calculateKey(final JsonObject jsonBody) {
        /*
         * Data Processing
         */
        final Object dataPart = jsonBody.getValue("data");
        final TreeMap<String, String> dataMap = this.dataMap((JsonObject) dataPart, jsonBody.getJsonArray("key"));

        /*
         * Cache Key
         */
        return this.dataUnique(jsonBody.getString("type"), KEY_DATA, dataMap);
    }
}
