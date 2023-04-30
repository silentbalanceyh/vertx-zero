package io.horizon.util;

import io.horizon.eon.VValue;
import io.horizon.fn.HFn;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author lang : 2023/4/30
 */
class CFind {
    static JsonObject find(final JsonArray array, final String field, final Object value) {
        return HaS.itJArray(array).filter(item -> {
            if (Objects.isNull(value)) {
                return Objects.isNull(item.getValue(field));
            } else {
                return value.equals(item.getValue(field));
            }
        }).findAny().orElse(null);
    }

    static JsonObject find(final JsonArray array, final JsonObject subsetQ) {
        return HaS.itJArray(array).filter(item -> {
            final Set<String> keys = subsetQ.fieldNames();
            final JsonObject subset = HaS.elementSubset(item, keys);
            return subset.equals(subsetQ);
        }).findAny().orElse(new JsonObject());
    }

    static <T> T find(final List<T> list, final Predicate<T> fnFilter) {
        return HFn.runOr(() -> {
            final List<T> filtered = list.stream().filter(fnFilter).toList();
            return HFn.runOr(filtered.isEmpty(),
                () -> null,
                () -> filtered.get(VValue.IDX));
        }, list, fnFilter);
    }
}
