package io.horizon.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author lang : 2023/4/30
 */
class CMap {

    static <K, V, E> ConcurrentMap<K, V> map(final List<E> list, final Function<E, K> keyFn, final Function<E, V> valueFn) {
        final ConcurrentMap<K, V> grouped = new ConcurrentHashMap<>();
        if (Objects.nonNull(list)) {
            list.stream().filter(Objects::nonNull).forEach(each -> {
                final K key = keyFn.apply(each);
                final V value = valueFn.apply(each);
                if (Objects.nonNull(key) && Objects.nonNull(value)) {
                    grouped.put(key, value);
                }
            });
        }
        return grouped;
    }

    static ConcurrentMap<String, JsonObject> map(final JsonArray data, final String field) {
        final ConcurrentMap<String, JsonObject> mapped = new ConcurrentHashMap<>();
        HaS.itJArray(data).forEach(json -> {
            final String key = json.getString(field);
            if (HaS.isNotNil(key)) {
                mapped.put(key, json.copy());
            }
        });
        return mapped;
    }

    @SuppressWarnings("unchecked")
    static <T> ConcurrentMap<String, T> map(final JsonArray data, final String field, final String to) {
        final ConcurrentMap<String, T> mapped = new ConcurrentHashMap<>();
        HaS.itJArray(data).forEach(json -> {
            final String key = json.getString(field);
            /*
             * Fix Issue:
             * java.lang.NullPointerException
             *      at java.base/java.util.concurrent.ConcurrentHashMap.putVal(ConcurrentHashMap.java:1011)
             *      at java.base/java.util.concurrent.ConcurrentHashMap.put(ConcurrentHashMap.java:1006)
             */
            if (HaS.isNotNil(key) && Objects.nonNull(json.getValue(to))) {
                mapped.put(key, (T) json.getValue(to));
            }
        });
        return mapped;
    }
}
