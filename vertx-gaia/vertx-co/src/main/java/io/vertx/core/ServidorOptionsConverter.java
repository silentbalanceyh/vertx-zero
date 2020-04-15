package io.vertx.core;

import io.vertx.core.json.JsonObject;

final class ServidorOptionsConverter {

    private ServidorOptionsConverter() {
    }

    static void fromJson(final JsonObject json, final ServidorOptions obj) {
        if (json.getValue("name") instanceof String) {
            obj.setName(json.getString("name"));
        }
        if (json.getValue("config") instanceof JsonObject) {
            final JsonObject config = json.getJsonObject("config").copy();
            if (config.getValue("host") instanceof String) {
                obj.setHost(config.getString("host"));
            }
            if (config.getValue("port") instanceof Integer) {
                obj.setPort(config.getInteger("port"));
            }
            config.remove("host");
            config.remove("port");
            obj.setOptions(config);
        }
    }
}
