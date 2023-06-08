package io.horizon.util;

import io.vertx.core.json.JsonArray;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang : 2023/4/30
 */
class CCount {

    static ConcurrentMap<String, Integer> count(final JsonArray array, final Set<String> fields) {
        final ConcurrentMap<String, Integer> counter = new ConcurrentHashMap<>();
        fields.forEach(field -> {
            final Set<String> set = HUt.valueSetString(array, field);
            counter.put(field, set.size());
        });
        return counter;
    }
}
