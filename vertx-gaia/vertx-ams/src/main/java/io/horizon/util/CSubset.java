package io.horizon.util;

import io.horizon.fn.HFn;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author lang : 2023/4/30
 */
class CSubset {

    // 子集
    static JsonArray subset(final JsonArray array, final Predicate<JsonObject> matchFn) {
        return HFn.runOr(new JsonArray(), () -> {
            final JsonArray subset = new JsonArray();
            HUt.itJArray(array).filter(matchFn).forEach(subset::add);
            return subset;
        }, array, matchFn);
    }

    static JsonObject subset(final JsonObject input, final Set<String> pickedKeys) {
        if (Objects.isNull(input) || Objects.isNull(pickedKeys) || pickedKeys.isEmpty()) {
            return new JsonObject();
        } else {
            final JsonObject normalized = new JsonObject(); // input.copy();
            pickedKeys.forEach(field -> normalized.put(field, input.getValue(field)));
            return normalized;
        }
    }

    static JsonArray subset(final JsonArray array, final Set<String> pickedKeys) {
        final JsonArray updated = new JsonArray();
        HIter.itJArray(array).filter(Objects::nonNull)
            .map(item -> subset(item, pickedKeys))
            .filter(item -> !TIs.isNil(item))
            .forEach(updated::add);
        return updated;
    }
}
