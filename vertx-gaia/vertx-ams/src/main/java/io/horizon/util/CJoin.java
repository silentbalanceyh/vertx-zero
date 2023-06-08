package io.horizon.util;

import io.horizon.fn.HFn;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

/**
 * @author lang : 2023/4/30
 */
class CJoin {
    static JsonArray join(final JsonArray source, final JsonArray target,
                          final String sourceKey, final String targetKey) {
        final JsonArray result = new JsonArray();
        final JsonArray sourceA = HJson.valueJArray(source, false);
        HFn.jvmAt(() -> sourceA.stream()
            .filter(Objects::nonNull)
            .map(item -> (JsonObject) item)
            .map(item -> item.mergeIn(findByKey(target, targetKey, item.getValue(sourceKey))))
            .forEach(result::add), target, sourceKey, targetKey);
        return result;
    }

    private static JsonObject findByKey(final JsonArray source,
                                        final String key,
                                        final Object value) {
        return HFn.failOr(() -> source.stream()
            .filter(Objects::nonNull)
            .map(item -> (JsonObject) item)
            .filter(item -> null != item.getValue(key))
            .filter(item -> value == item.getValue(key) || item.getValue(key).equals(value))
            .findFirst().orElse(new JsonObject()), source, key);
    }
}
