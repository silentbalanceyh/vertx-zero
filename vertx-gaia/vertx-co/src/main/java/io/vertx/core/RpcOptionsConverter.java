package io.vertx.core;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;

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
 * Converter for {@link RpcOptions}
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class RpcOptionsConverter {

    private RpcOptionsConverter() {
    }

    static void fromJson(final JsonObject json, final RpcOptions obj) {
        if (json.getValue(KName.NAME) instanceof String) {
            obj.setName(json.getString(KName.NAME));
        }
        if (json.getValue(KName.CONFIG) instanceof JsonObject) {
            final JsonObject config = json.getJsonObject(KName.CONFIG).copy();
            if (config.getValue(KName.HOST) instanceof String) {
                obj.setHost(config.getString(KName.HOST));
            }
            if (config.getValue(KName.PORT) instanceof Integer) {
                obj.setPort(config.getInteger(KName.PORT));
            }
            config.remove(KName.HOST);
            config.remove(KName.PORT);
            obj.setOptions(config);
        }
    }
}
