package io.vertx.up.plugin.cache.hit;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
abstract class AbstractL1Algorithm implements L1Algorithm {

    /*
     * Get DATA_REF record
     * Non-Primary Key processing
     */
    @Override
    public String dataRefKey(final String type, final JsonObject condition) {
        final TreeMap<String, String> treeMap = this.dataMap(condition);
        return this.dataKey(type, CACHE_DATA_REF, null, treeMap);
    }

    /*
     * Get DATA directly
     * Primary Key processing
     */
    @Override
    public String dataKey(final String type, final JsonObject condition) {
        final TreeMap<String, String> treeMap = this.dataMap(condition);
        return this.dataKey(type, treeMap);
    }

    @Override
    public String dataKey(final String type, final TreeMap<String, String> treeMap) {
        return this.dataKey(type, CACHE_DATA, this.dataType(), treeMap);
    }

    /*
     * Get TREE directly
     */

    @Override
    public String dataTreeKey(final String type, final TreeMap<String, String> treeMap) {
        final String dataKey = this.dataKey(type, treeMap);
        return this.dataTreeKey(dataKey, type);
    }

    @Override
    public ConcurrentMap<String, Object> buildData(final JsonObject jsonBody) {
        final ConcurrentMap<String, Object> resultMap = new ConcurrentHashMap<>();
        /*
         * Get refer attribute to check whether contains `reference`
         */
        final Boolean isRefer = jsonBody.getBoolean(FIELD_REFER, Boolean.FALSE);
        /*
         * Data processing for different data part
         * 1) JsonObject - Single Record
         * 2) JsonArray - Collection Data
         */
        this.dataProcess(resultMap, jsonBody);
        /*
         * Data refer processing
         */
        if (isRefer && Ut.isNotNil(jsonBody.getJsonObject(FIELD_CONDITION))) {
            /*
             * Call refer here
             * cacheKey = dataKey here
             */
            this.dataRefer(resultMap, jsonBody);
        }
        return resultMap;
    }

    @Override
    public ConcurrentMap<String, Object> buildReference(final JsonObject jsonBody) {
        final ConcurrentMap<String, Object> resultMap = new ConcurrentHashMap<>();
        /*
         * Get refer attribute to check whether need `dataKey` step
         * refer = true will trigger this method
         */
        final Boolean isRefer = jsonBody.getBoolean(FIELD_REFER, Boolean.FALSE);
        if (isRefer) {
            /*
             * Data Tree processing
             */
            this.dataTree(resultMap, jsonBody);
        }
        return resultMap;
    }

    public void dataRefer(final ConcurrentMap<String, Object> resultMap, final JsonObject jsonBody) {
        // Optional for RECORD / COLLECTION in different processing workflow
    }

    /*
     * The same for original calculation
     * on element of map ( key = value )
     */

    protected String dataKey(final String type, final String prefix, final String dataType, final TreeMap<String, String> dataMap) {
        final StringBuilder key = new StringBuilder();
        /*
         * Group Redis by : character here
         */
        key.append(type).append(":");
        key.append(prefix).append(":");
        if (Ut.isNotNil(dataType)) {
            key.append(dataType).append(":");
        }
        dataMap.forEach((k, v) -> key.append(k).append("=").append(v).append(CNODE_CONNECTOR));
        return key.toString();
    }

    protected String dataTreeKey(final String dataKey, final JsonObject jsonBody) {
        return this.dataTreeKey(dataKey, jsonBody.getString(FIELD_TYPE));
    }

    protected String dataTreeKey(final String dataKey, final String type) {
        return type + /* ":" + this.dataType() + */ ":" + CACHE_DATA_TREE + ":" + dataKey;
    }

    /*
     * Convert to TreeMap here
     */
    protected TreeMap<String, String> dataMap(final JsonObject condition) {
        final TreeMap<String, String> treeMap = new TreeMap<>();
        condition.fieldNames().forEach(field -> {
            final Object value = condition.getValue(field);
            if (Objects.nonNull(value)) {
                treeMap.put(field, value.toString());
            }
        });
        return treeMap;
    }

    protected TreeMap<String, String> dataMap(final JsonObject record, final JsonArray key) {
        final TreeSet<String> primaryKey = new TreeSet<>(Ut.toSet(key));
        final TreeMap<String, String> treeMap = new TreeMap<>();
        primaryKey.forEach(field -> {
            final Object value = record.getValue(field);
            if (Objects.nonNull(value)) {
                treeMap.put(field, value.toString());
            }
        });
        return treeMap;
    }


    /*
     * Abstract Processing data
     */
    public abstract String dataType();

    /*
     * Abstract Processing data body
     */
    public abstract void dataProcess(ConcurrentMap<String, Object> resultMap, JsonObject jsonBody);

    public abstract void dataTree(ConcurrentMap<String, Object> resultMap, JsonObject jsonBody);
}
