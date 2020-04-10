package io.vertx.up.uca.yaml;

import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;

public class ZeroInfix implements Node<JsonObject> {

    private transient final String key;

    ZeroInfix(final String key) {
        this.key = key;
    }

    @Override
    public JsonObject read() {
        // Not null because execNil
        final JsonObject config = ZeroTool.read(key, true);
        return Fn.getJvm(new JsonObject(), () -> config, config);
    }
}
