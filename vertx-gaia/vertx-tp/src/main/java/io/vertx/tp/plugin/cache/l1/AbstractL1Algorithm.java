package io.vertx.tp.plugin.cache.l1;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.hit.CacheMeta;
import io.vertx.up.eon.em.ChangeFlag;

import java.util.Objects;
import java.util.TreeMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public abstract class AbstractL1Algorithm implements L1Algorithm {
    private static final String KEY_DATA = "DATA";
    private static final String KEY_DATA_REF = "DATA_REF";

    /*
     * Phase 1: Data Delivery
     */
    @Override
    public <T> Buffer dataDelivery(final T entity, final ChangeFlag flag, final CacheMeta meta) {
        final JsonObject data = new JsonObject();
        /*
         * Check entity to see whether they are collection
         * New structure for different data set
         */
        this.dataDelivery(data, entity, meta);
        /*
         * Flag processing
         */
        data.put("flag", flag);
        /*
         * Key Metadata
         */
        data.mergeIn(meta.metadata());
        /*
         * HKey generate
         */
        return data.toBuffer();
    }

    @Override
    public String dataUnique(final String type, final JsonObject condition) {
        final TreeMap<String, String> treeMap = this.dataUnique(condition);
        return this.dataUnique(type, KEY_DATA, treeMap);
    }

    @Override
    public String dataUnique(final String type, final TreeMap<String, String> treeMap) {
        return this.dataUnique(type, KEY_DATA, treeMap);
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

    public abstract <T> void dataDelivery(final JsonObject message, final T entity, final CacheMeta meta);
}
