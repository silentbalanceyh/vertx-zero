package io.vertx.core;

import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

/**
 * 「Tp」 Vert.x Extension
 *
 * This class is `Converter` class of `Options`, it just like any other converters inner
 * vert.x framework. In vert.x framework, each `XOptions` contains at least one converter to
 * process `JsonObject` configuration data. It provide feature to to type checking and default
 * value injection.
 *
 * This class is ClusterOptions assist tool
 *
 * * enabled: Boolean Type
 * * manager: String class and it will be converted to `ClusterManager`
 * * options: JsonObject
 *
 * @author lang
 */
final class ClusterOptionsConverter {
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
