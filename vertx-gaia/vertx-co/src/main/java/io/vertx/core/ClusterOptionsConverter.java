package io.vertx.core;

import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.up.util.Ut;
import io.vertx.up.fn.Fn;

class ClusterOptionsConverter {
    ClusterOptionsConverter() {
    }

    static void fromJson(final JsonObject json, final ClusterOptions obj) {
        if (json.getValue("enabled") instanceof Boolean) {
            obj.setEnabled(json.getBoolean("enabled"));
        }
        if (json.getValue("options") instanceof JsonObject) {
            obj.setOptions(json.getJsonObject("options"));
        }
        final Object managerObj = json.getValue("manager");
        Fn.safeNull(() -> {
            final Class<?> clazz = Ut.clazz(managerObj.toString());
            Fn.safeNull(() -> {
                // If null, keep default
                final ClusterManager manager = Ut.instance(clazz);
                obj.setManager(manager);
            }, clazz);
        }, managerObj);
    }
}
