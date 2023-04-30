package io.horizon.util;

import io.vertx.core.json.JsonArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang : 2023/4/30
 */
class CFlat {
    static JsonArray flat(final JsonArray item) {
        final JsonArray normalized = new JsonArray();
        item.stream().filter(Objects::nonNull).forEach(each -> {
            if (each instanceof JsonArray) {
                normalized.addAll(flat((JsonArray) each));
            } else {
                normalized.add(each);
            }
        });
        return normalized;
    }

    static <K, V> ConcurrentMap<K, List<V>> flat(final List<ConcurrentMap<K, List<V>>> dataList) {
        final ConcurrentMap<K, List<V>> resultMap = new ConcurrentHashMap<>();
        dataList.forEach(each -> each.forEach((k, vList) -> {
            /*
             * Original Key Extraction
             */
            if (Objects.nonNull(k)) {
                /*
                 * Contains k checking for result map
                 */
                if (!resultMap.containsKey(k)) {
                    resultMap.put(k, new ArrayList<>());
                }
                final List<V> ref = resultMap.get(k);
                vList.stream().filter(Objects::nonNull).forEach(ref::add);
                resultMap.put(k, ref);  // Replace
            }
        }));
        return resultMap;
    }
}
