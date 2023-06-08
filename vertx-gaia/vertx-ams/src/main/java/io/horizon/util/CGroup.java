package io.horizon.util;

import io.horizon.eon.VValue;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author lang : 2023/4/30
 */
class CGroup {
    static <K, V, E> ConcurrentMap<K, List<V>> group(final Collection<E> object, final Function<E, K> keyFn, final Function<E, V> valueFn) {
        final ConcurrentMap<K, List<V>> ret = new ConcurrentHashMap<>();
        if (Objects.nonNull(object) && !object.isEmpty()) {
            for (final E item : object) {
                if (null != item) {
                    final K key = keyFn.apply(item);
                    if (null != key) {
                        // Extract List
                        List<V> reference = null;
                        if (ret.containsKey(key)) {
                            reference = ret.get(key);
                        }
                        // Double check
                        if (null == reference) {
                            reference = new ArrayList<>();
                        }
                        final V value = valueFn.apply(item);
                        if (null != value) {
                            reference.add(value);
                        }
                        // Replace finally
                        ret.put(key, reference);
                    }
                }
            }
        }
        return ret;
    }

    static ConcurrentMap<String, JsonArray> group(final JsonArray source, final Function<JsonObject, String> executor) {
        final ConcurrentMap<String, JsonArray> ret = new ConcurrentHashMap<>();
        if (Objects.nonNull(source) && !source.isEmpty()) {
            source.stream().filter(Objects::nonNull)
                .map(item -> (JsonObject) item)
                .filter(item -> Objects.nonNull(executor.apply(item)))
                .forEach(item -> {
                    /*
                     * JsonArray get
                     */
                    final String key = executor.apply(item);
                    /*
                     * `key` calculated here for map final `key`
                     */
                    JsonArray reference = ret.get(key);
                    if (Objects.isNull(reference)) {
                        reference = new JsonArray();
                        ret.put(key, reference);
                    }
                    /*
                     * Add new Object
                     */
                    reference.add(item.copy());
                });
        }
        return ret;
    }

    static List<JsonArray> group(final JsonArray source, final Integer size) {
        final List<JsonArray> groupData = new ArrayList<>();
        final JsonArray container = new JsonArray();
        final int length = source.size();
        for (int index = VValue.IDX; index < length; index++) {
            final JsonObject json = source.getJsonObject(index);
            container.add(json);
            if (0 == (index + 1) % size) {
                //使用了copy方法，以避免紧接着的第二步清空刚添加的container；下方的if内同理
                groupData.add(container.copy());
                container.clear();
            }
        }
        if (!container.isEmpty()) {
            groupData.add(container.copy());
            container.clear();
        }
        return groupData;
    }
}
