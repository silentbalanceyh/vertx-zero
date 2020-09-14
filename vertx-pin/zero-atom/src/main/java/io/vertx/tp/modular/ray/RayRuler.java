package io.vertx.tp.modular.ray;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.reference.DataQRule;
import io.vertx.up.atom.Kv;
import io.vertx.up.commune.Record;
import io.vertx.up.commune.element.AmbJson;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class RayRuler {
    /*
     * Grouped by joined key
     */
    static ConcurrentMap<String, AmbJson> group(final JsonArray source, final List<Kv<String, String>> joined,
                                                final Class<?> type) {
        final ConcurrentMap<String, AmbJson> grouped = new ConcurrentHashMap<>();
        if (JsonObject.class == type) {
            /*
             * 单记录
             */
            Ut.itJArray(source).forEach(json -> {
                final String key = joinedKey(json, joined);
                if (Ut.notNil(key)) {
                    grouped.put(key, new AmbJson().data(json));
                }
            });
        } else {
            /*
             * 多记录
             */
            final ConcurrentMap<String, JsonArray> groupedArray = new ConcurrentHashMap<>();
            Ut.itJArray(source).forEach(json -> {
                final String key = joinedKey(json, joined);
                if (Ut.notNil(key)) {
                    final JsonArray group = Fn.pool(groupedArray, key, JsonArray::new);
                    group.add(json);
                }
            });
            groupedArray.forEach((key, data) -> {
                final AmbJson amb = new AmbJson().data(data);
                grouped.put(key, amb);
            });
        }
        return grouped;
    }

    /*
     * Required
     */
    static AmbJson required(final AmbJson amb, final DataQRule rule) {
        /* required 字段提取 */
        final Boolean isSingle = amb.isSingle();
        if (Objects.isNull(isSingle)) {
            /*
             * invalid
             */
            return null;
        } else {
            if (isSingle) {
                final JsonObject data = compress(amb.dataT(), rule);
                if (Objects.nonNull(data)) {
                    amb.data(data);
                } else {
                    /*
                     * invalid
                     */
                    return null;
                }
            } else {
                final JsonArray dataArray = amb.dataT();
                final JsonArray normalized = new JsonArray();
                Ut.itJArray(dataArray).map(json -> compress(json, rule))
                        .filter(Objects::nonNull).forEach(normalized::add);
                amb.data(dataArray);
            }
        }
        return amb;
    }

    /*
     * value for Json
     * key for Record
     */
    private static String joinedKey(final JsonObject item, final List<Kv<String, String>> joined) {
        final StringBuilder key = new StringBuilder();
        joined.forEach(kv -> {
            final Object value = item.getValue(kv.getKey());
            if (Objects.nonNull(value)) {
                key.append(value);
            }
        });
        return key.toString();
    }

    static String joinedKey(final Record record, final List<Kv<String, String>> joined) {
        final StringBuilder key = new StringBuilder();
        joined.forEach(kv -> {
            final Object value = record.get(kv.getValue());
            if (Objects.nonNull(value)) {
                key.append(value);
            }
        });
        return key.toString();
    }

    private static JsonObject compress(final JsonObject item, final DataQRule rule) {
        /* required 字段提取 */
        final JsonArray required = rule.getRequired();
        if (Ut.notNil(required)) {
            /* Set<String> for required */
            final Set<String> fields = Ut.toSet(required);
            if (isSatisfy(item, fields)) {
                return item;
            } else {
                /*
                 * Validation failure
                 */
                return null;
            }
        } else return item;
    }

    private static boolean isSatisfy(final JsonObject item, final Set<String> requiredSet) {
        return requiredSet.stream().allMatch(field -> {
            final Object value = item.getValue(field);
            if (Objects.nonNull(value)) {
                return Ut.notNil(value.toString());
            } else return false;
        });
    }
}
