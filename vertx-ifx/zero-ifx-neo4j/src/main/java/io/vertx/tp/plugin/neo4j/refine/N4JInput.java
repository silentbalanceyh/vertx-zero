package io.vertx.tp.plugin.neo4j.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class N4JInput {
    /*
     * Input parameters
     */
    static Value parameters(final JsonObject data) {
        final List<Object> keyValue = new ArrayList<>();
        Ut.itJObject(data, (value, field) -> {
            keyValue.add(field);
            if (value instanceof JsonObject) {
                keyValue.add(((JsonObject) value).encode());
            } else {
                keyValue.add(value);
            }
        });
        return Values.parameters(keyValue.toArray());
    }

    static JsonArray marker(final JsonArray node) {
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(node).map(N4JInput::marker).forEach(normalized::add);
        return normalized;
    }

    static JsonObject marker(final JsonObject node) {
        /*
         * 1. name does not existing
         */
        final JsonObject replaced = node.copy();
        if (!replaced.containsKey("name")) {
            Object value = replaced.getValue("code");
            final String name;
            if (Objects.isNull(value)) {
                value = replaced.getValue("key");
                if (Objects.isNull(value)) {
                    name = "node-" + replaced.hashCode();
                } else {
                    name = value.toString();
                }
            } else {
                name = value.toString();
            }
            replaced.put("name", name);
        }
        return replaced;
    }
}
