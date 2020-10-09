package io.vertx.tp.plugin.cache.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class CacheTool {
    private static final String KEY_DATA = "DATA";
    private static final String KEY_DATA_REF = "DATA_REF";
    private static final String KEY_DATA_TREE = "DATA_TREE";

    /*
     * Delivery when event bus consume ( send data )
     */
    public static ConcurrentMap<String, Object> generateData(final JsonObject data) {
        final ConcurrentMap<String, Object> resultMap = new ConcurrentHashMap<>();
        /*
         * Data Part Processing
         * 1) Get primary = true / false
         */
        final Boolean primary = data.getBoolean("primary", Boolean.TRUE);
        final String type = data.getString("type");
        /*
         * key = data
         */
        final Object dataPart = data.getValue("data");
        /*
         * key calculation by primaryKey
         * This situation the dataPart must be JsonObject
         */
        if (dataPart instanceof JsonObject) {
            /*
             * primaryKey = data
             */
            final String cacheKey = keyId((JsonObject) dataPart, type, data.getJsonArray("key"));
            resultMap.put(cacheKey, dataPart);
        } else if (dataPart instanceof JsonArray) {
            /*
             * Cache key for collection
             */

        }
        if (!primary) {
            /*
             * cacheKey = dataKey Ref
             */
            keyRef(resultMap, data, false);
        }
        return resultMap;
    }

    public static ConcurrentMap<String, Object> generateKey(final JsonObject data) {
        /*
         * Data Part Processing
         * 1) Get primary = true / false
         */
        final Boolean primary = data.getBoolean("primary", Boolean.TRUE);
        if (!primary) {
            final String type = data.getString("type");
            final ConcurrentMap<String, Object> resultMap = new ConcurrentHashMap<>();
            /*
             * key = cacheKey
             */
            keyRef(resultMap, data, true);
            /*
             * Process resultMap on existing references
             */
            final ConcurrentMap<String, Object> calculated = new ConcurrentHashMap<>();
            resultMap.forEach((key, value) -> {
                /*
                 * TREE Key
                 */
                final String normalizedKey = type + ":" + KEY_DATA_TREE + ":" + key;
                calculated.put(normalizedKey, value);
            });
            return calculated;
        } else {
            return new ConcurrentHashMap<>();
        }
    }

    public static String keyCond(final String type, final JsonObject condition) {
        final TreeMap<String, String> treeMap = keyCond(condition);
        return keyUniform(type, KEY_DATA_REF, treeMap);
    }

    public static TreeMap<String, String> keyCond(final JsonObject condition) {
        final TreeMap<String, String> treeMap = new TreeMap<>();
        condition.fieldNames().forEach(field -> {
            final Object value = condition.getValue(field);
            if (Objects.nonNull(value)) {
                treeMap.put(field, value.toString());
            }
        });
        return treeMap;
    }

    /*
     * The key that related to JsonArray
     */
    private static String keyUniform(final String type, final String prefix, final TreeMap<String, String> treeMap) {
        final StringBuilder key = new StringBuilder();
        /*
         * Group Redis by : character here
         */
        key.append(type).append(":").append(prefix).append(":");
        treeMap.forEach((k, v) -> key.append(k).append("=").append(v).append(","));
        return key.toString();
    }

    private static String keyId(final JsonObject record, final String type, final JsonArray key) {
        final TreeMap<String, String> treeMap = keyMap(record, key);
        return keyUniform(type, KEY_DATA, treeMap);
    }

    private static TreeMap<String, String> keyMap(final JsonObject record, final JsonArray key) {
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

    private static void keyRef(final ConcurrentMap<String, Object> keyMap,
                               final JsonObject data, final boolean reverted) {
        final String type = data.getString("type");
        final Object dataPart = data.getValue("data");
        final JsonObject condition = data.getJsonObject("condition");
        if (dataPart instanceof JsonObject) {
            /*
             * condition building
             */
            final String cacheKey = keyCond(type, condition);
            if (reverted) {
                /*
                 * Tree for cache
                 */
                final String cacheValue = keyId((JsonObject) dataPart, type, data.getJsonArray("key"));
                keyMap.put(cacheValue, cacheKey);
            } else {
                final String cacheValue = keyId((JsonObject) dataPart, type, data.getJsonArray("key"));
                keyMap.put(cacheKey, cacheValue);
            }
        }
    }
}
