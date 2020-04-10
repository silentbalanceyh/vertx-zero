package io.vertx.tp.plugin.neo4j.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

class N4JCond {
    static JsonObject nodeUnique(final JsonObject data) {
        final JsonObject condition = new JsonObject();
        if (data.containsKey("key")) {
            condition.put("key", data.getValue("key"));
        }
        if (data.containsKey("code")) {
            condition.put("code", data.getValue("code"));
        }
        return condition;
    }

    static JsonArray nodeUnique(final JsonArray data) {
        final JsonArray condition = new JsonArray();
        Ut.itJArray(data).map(N4JCond::nodeUnique).forEach(condition::add);
        return condition;
    }
}
