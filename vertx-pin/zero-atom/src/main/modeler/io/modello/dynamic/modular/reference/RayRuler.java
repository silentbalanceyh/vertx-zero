package io.modello.dynamic.modular.reference;

import io.horizon.atom.common.Kv;
import io.horizon.uca.cache.Cc;
import io.modello.specification.HRecord;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.element.JAmb;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ## Rule Applier
 *
 * ### 1. Intro
 *
 * This class validate each record related to current attribute.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class RayRuler {

    static ConcurrentMap<String, JAmb> group(final JsonArray source, final List<Kv<String, String>> joined,
                                             final Class<?> type) {
        final ConcurrentMap<String, JAmb> grouped = new ConcurrentHashMap<>();
        if (JsonObject.class == type) {
            /*
             * 单记录
             */
            Ut.itJArray(source).forEach(json -> {
                final String key = joinedKey(json, joined);
                if (Ut.isNotNil(key)) {
                    grouped.put(key, new JAmb().data(json));
                }
            });
        } else {
            /*
             * 多记录
             */
            final ConcurrentMap<String, JsonArray> groupedArray = new ConcurrentHashMap<>();
            Ut.itJArray(source).forEach(json -> {
                final String key = joinedKey(json, joined);
                if (Ut.isNotNil(key)) {
                    final JsonArray group = Cc.pool(groupedArray, key, JsonArray::new);
                    group.add(json);
                }
            });
            groupedArray.forEach((key, data) -> {
                final JAmb amb = new JAmb().data(data);
                grouped.put(key, amb);
            });
        }
        return grouped;
    }

    /*
     * value for Json
     * key for Record
     */
    private static String joinedKey(final JsonObject item, final List<Kv<String, String>> joined) {
        final StringBuilder key = new StringBuilder();
        joined.forEach(kv -> {
            final Object value = item.getValue(kv.key());
            if (Objects.nonNull(value)) {
                key.append(value);
            }
        });
        return key.toString();
    }

    static String joinedKey(final HRecord record, final List<Kv<String, String>> joined) {
        final StringBuilder key = new StringBuilder();
        joined.forEach(kv -> {
            final Object value = record.get(kv.value());
            if (Objects.nonNull(value)) {
                key.append(value);
            }
        });
        return key.toString();
    }
}
