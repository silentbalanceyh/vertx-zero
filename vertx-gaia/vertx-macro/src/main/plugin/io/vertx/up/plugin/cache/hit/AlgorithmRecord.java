package io.vertx.up.plugin.cache.hit;

import io.vertx.core.json.JsonObject;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AlgorithmRecord extends AbstractL1Algorithm {

    @Override
    public String dataType() {
        return CNODE_RECORD;
    }

    @Override
    public void dataProcess(final ConcurrentMap<String, Object> resultMap, final JsonObject jsonBody) {
        final Object dataPart = jsonBody.getValue(FIELD_DATA);
        /*
         * DATA
         */
        final String cacheKey = this.calculateKey(jsonBody);
        /*
         * Cache Communication
         */
        resultMap.put(cacheKey, dataPart);           // Cache Write
    }

    @Override
    public void dataRefer(final ConcurrentMap<String, Object> resultMap, final JsonObject jsonBody) {
        final JsonObject condition = jsonBody.getJsonObject(FIELD_CONDITION);
        /*
         * conditionKey = dataKey
         */
        final String cacheKey = this.calculateKey(jsonBody);
        /*
         * conditionKey will put into
         */
        final String conditionKey = this.dataRefKey(jsonBody.getString(FIELD_TYPE), condition);
        resultMap.put(conditionKey, cacheKey);
    }

    @Override
    public void dataTree(final ConcurrentMap<String, Object> resultMap, final JsonObject jsonBody) {
        /*
         * dataKey = conditionKey
         */
        final JsonObject condition = jsonBody.getJsonObject(FIELD_CONDITION);
        final String conditionKey = this.dataRefKey(jsonBody.getString(FIELD_TYPE), condition);
        final String cacheKey = this.calculateKey(jsonBody);
        /*
         * Condition Key will append
         */
        resultMap.put(this.dataTreeKey(cacheKey, jsonBody), conditionKey);
    }

    private String calculateKey(final JsonObject jsonBody) {
        /*
         * Data Processing
         */
        final Object dataPart = jsonBody.getValue(FIELD_DATA);
        final TreeMap<String, String> dataMap = this.dataMap((JsonObject) dataPart, jsonBody.getJsonArray(FIELD_KEY));

        /*
         * Cache Key
         */
        return this.dataKey(jsonBody.getString(FIELD_TYPE), CACHE_DATA, this.dataType(), dataMap);
    }
}
