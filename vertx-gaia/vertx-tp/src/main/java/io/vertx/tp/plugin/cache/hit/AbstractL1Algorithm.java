package io.vertx.tp.plugin.cache.hit;

import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
abstract class AbstractL1Algorithm implements L1Algorithm {
    private static final String KEY_DATA = "DATA";
    private static final String KEY_DATA_REF = "DATA_REF";

    @Override
    public String dataUnique(final String type, final JsonObject condition) {
        final TreeMap<String, String> treeMap = this.dataUnique(condition);
        return this.dataUnique(type, KEY_DATA, treeMap);
    }

    @Override
    public String dataUnique(final String type, final TreeMap<String, String> treeMap) {
        return this.dataUnique(type, KEY_DATA, treeMap);
    }

    @Override
    public ConcurrentMap<String, Object> dataCache(final JsonObject jsonBody) {
        final ConcurrentMap<String, Object> resultMap = new ConcurrentHashMap<>();

        return resultMap;
    }

    @Override
    public ConcurrentMap<String, Object> dataKey(final JsonObject jsonBody) {
        final ConcurrentMap<String, Object> resultMap = new ConcurrentHashMap<>();

        return resultMap;
    }

    /*
     * The same for original calculation
     * on element of map ( key = value )
     */
    protected String dataUnique(final String type, final String prefix, final TreeMap<String, String> dataMap) {
        final StringBuilder key = new StringBuilder();
        /*
         * Group Redis by : character here
         */
        key.append(type).append(":").append(prefix).append(":").append(this.dataType()).append(":");
        dataMap.forEach((k, v) -> key.append(k).append("=").append(v).append(","));
        return key.toString();
    }

    protected TreeMap<String, String> dataUnique(final JsonObject condition) {
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
     * Abstract Processing data
     */
    public abstract String dataType();
}
