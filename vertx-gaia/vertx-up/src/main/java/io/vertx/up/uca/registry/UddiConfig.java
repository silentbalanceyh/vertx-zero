package io.vertx.up.uca.registry;

import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

class UddiConfig {
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);

    static Class<?> registry() {
        final JsonObject data = uddiJson();
        final String component = data.getString("registry");
        return Ut.clazz(component, null);
    }

    static Class<?> jet() {
        final JsonObject data = uddiJson();
        final String component = data.getString("discovery");
        return Ut.clazz(component, null);
    }

    static Class<?> client() {
        final JsonObject data = uddiJson();
        final String component = data.getString("client");
        return Ut.clazz(component, null);
    }

    private static JsonObject uddiJson() {
        final JsonObject config = VISITOR.read();
        /*
         * configuration in vertx-tp.yml
         */
        final JsonObject data = config.getJsonObject("uddi");
        return Ut.sureJObject(data);
    }
}
