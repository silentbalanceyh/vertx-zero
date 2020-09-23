package io.vertx.tp.plugin.cache.l1;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.hit.HMeta;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class L1Kit {
    private static final String KEY_DAO = "KEY_DAO";
    private static final String KEY_QUERY = "KEY_QUERY";

    /*
     * Delivery when event bus publish ( send data )
     */
    public static <T> Buffer dataDelivery(final T entity, final ChangeFlag flag,
                                          final HMeta meta) {
        final JsonObject data = new JsonObject();
        data.put("data", (JsonObject) Ut.serializeJson(entity));
        data.put("type", meta.typeName());
        data.put("keys", meta.keys());
        data.put("flag", flag);
        /*
         * HKey generate
         */
        return data.toBuffer();
    }

    /*
     * Delivery when event bus consume ( send data )
     */
    public static ConcurrentMap<String, JsonObject> dataConsumer(final JsonObject data, final L1Config config) {
        final ConcurrentMap<String, JsonObject> resultMap = new ConcurrentHashMap<>();
        // Build data consumer
        final JsonArray keys = data.getJsonArray("keys");
        final JsonObject record = data.getJsonObject("data");
        Ut.itJArray(keys, JsonArray.class, (each, index) -> {
            // Build key for current
            final TreeMap<String, String> treeMap = new TreeMap<>();
            final int size = each.size();
            for (int idx = 0; idx < size; idx++) {
                final String literal = each.getString(idx);
                final Object value = record.getValue(literal);
                if (Objects.isNull(value)) {
                    treeMap.clear();
                    break;
                } else {
                    treeMap.put(literal, value.toString());
                }
            }
            if (!treeMap.isEmpty()) {
                final String cacheKey = keyString(data.getString("type"), treeMap);
                resultMap.put(cacheKey, record);
            }
        });
        return resultMap;
    }

    /*
     * Key Calculation
     */
    public static String keyString(final String type, final TreeMap<String, String> treeMap) {
        final StringBuilder key = new StringBuilder();
        key.append(KEY_DAO).append(":").append(type).append(":");
        treeMap.forEach((k, v) -> key.append(k).append("=").append(v).append(","));
        return key.toString();
    }
}
