package io.vertx.up.atom.exchange;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DictTool {

    static JsonArray process(final JsonArray process,
                             final Function<JsonObject, JsonObject> function) {
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(process).map(function).forEach(normalized::add);
        return normalized;
    }

    static JsonObject process(final ConcurrentMap<String, BMapping> dataMap,
                              final JsonObject input,
                              final BiFunction<BMapping, String, String> applier) {
        final JsonObject normalized = Objects.isNull(input) ? new JsonObject() : input.copy();
        dataMap.forEach((field, item) -> {
            final Object fromValue = input.getValue(field);
            if (Objects.nonNull(fromValue) && fromValue instanceof String) {
                final String toValue = applier.apply(item, fromValue.toString());
                normalized.put(field, toValue);
            }
        });
        return normalized;
    }
}
