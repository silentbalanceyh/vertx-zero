package io.vertx.up.plugin.cache.hit;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AlgorithmCollection extends AbstractL1Algorithm {

    @Override
    public String dataType() {
        return CNODE_LIST;
    }

    @Override
    public void dataProcess(final ConcurrentMap<String, Object> resultMap, final JsonObject jsonBody) {
        final Object dataPart = jsonBody.getValue(FIELD_DATA);
        /*
         * condition calculated
         */
        final String cacheKey = this.calculateKey(jsonBody);

        resultMap.put(cacheKey, dataPart);
    }

    @Override
    public void dataTree(final ConcurrentMap<String, Object> resultMap, final JsonObject jsonBody) {
        /*
         * dataKey = conditionKey
         */
        final String cacheKey = this.calculateKey(jsonBody);
        final Set<String> treeKeys = this.calculateTreeKey(jsonBody);
        treeKeys.forEach(key -> resultMap.put(key, cacheKey));
    }

    private String calculateKey(final JsonObject jsonBody) {
        final JsonObject condition = jsonBody.getJsonObject(FIELD_CONDITION);
        final TreeMap<String, String> treeMap = this.dataMap(condition);
        return this.dataKey(jsonBody.getString(FIELD_TYPE), treeMap);
    }

    private Set<String> calculateTreeKey(final JsonObject jsonBody) {
        final Object dataPart = jsonBody.getValue(FIELD_DATA);
        final Set<String> keys = new HashSet<>();
        if (dataPart instanceof JsonArray) {
            Ut.itJArray((JsonArray) dataPart).forEach(dataItem -> {
                final TreeMap<String, String> dataMap = this.dataMap(dataItem, jsonBody.getJsonArray(FIELD_KEY));
                /*
                 * Here the treeKey must be record
                 */
                final String dataKey = this.dataKey(jsonBody.getString(FIELD_TYPE), CACHE_DATA, CNODE_RECORD, dataMap);
                final String treeKey = this.dataTreeKey(dataKey, jsonBody);
                keys.add(treeKey);
            });
        }
        return keys;
    }
}
