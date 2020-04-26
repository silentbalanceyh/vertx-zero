package io.vertx.core;

import io.vertx.core.json.JsonObject;

/**
 * # 「Co」 Vert.x Extension
 *
 * This class is `Converter` class of `Options`, it should be generated but current one is not.
 *
 * This class is ServidorOptions assist tool
 *
 * * name: RPC Server name
 * * config: The default configuration for RPC Server
 * * config -> port: RPC Server port
 * * config -> host: RPC Server host
 *
 * Converter for {@link io.vertx.core.ServidorOptions}
 *
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
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
