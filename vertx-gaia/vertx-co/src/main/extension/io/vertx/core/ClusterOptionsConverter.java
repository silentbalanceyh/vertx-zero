package io.vertx.core;

import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

/**
 * # 「Co」 Vert.x Extension
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
 * Converter for {@link io.vertx.core.ClusterOptions}
 *
 * > NOTE: This class should be generated from {@link io.vertx.core.ClusterOptions} original class
 * using Vert.x codegen, but there exist `Class<?>` type attribute, the automatic generator has
 * been ignored.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class ClusterOptionsConverter {

    private ClusterOptionsConverter() {
    }

    static void fromJson(final JsonObject json, final ClusterOptions obj) {
        if (json.getValue("enabled") instanceof Boolean) {
            obj.setEnabled(json.getBoolean("enabled"));
        }
        if (json.getValue(KName.OPTIONS) instanceof JsonObject) {
            obj.setOptions(json.getJsonObject(KName.OPTIONS));
        }
        final Object managerObj = json.getValue("manager");
        Fn.runAt(() -> {
            final Class<?> clazz = Ut.clazz(managerObj.toString());
            Fn.runAt(() -> {
                // If null, keep default
                final ClusterManager manager = Ut.instance(clazz);
                obj.setManager(manager);
            }, clazz);
        }, managerObj);
    }
}
