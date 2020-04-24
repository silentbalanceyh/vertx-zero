package io.vertx.tp.plugin.shell.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.Infix;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class SlConfig {

    private static final JsonObject CONFIGURATION = new JsonObject();

    static void init() {
        /*
         * Initialize client
         */
        final JsonObject data = Infix.initTp("shell", config -> {
            /*
             * command configuration
             */
            final String command = config.getString("commands");
            config.put("commands", Ut.ioJArray(command));
            return config;
        }, JsonObject.class);
        CONFIGURATION.mergeIn(data, true);
    }

    static JsonObject welcome() {
        return Ut.sureJObject(CONFIGURATION.getJsonObject("welcome"));
    }

    static JsonObject validate() {
        return Ut.sureJObject(CONFIGURATION.getJsonObject("validate"));
    }

    static JsonArray commands() {
        return Ut.sureJArray(CONFIGURATION.getJsonArray("commands"));
    }

    static JsonArray commandsDefault() {
        return Ut.sureJArray(CONFIGURATION.getJsonArray("commandsDefault"));
    }

    static JsonObject commandsBack() {
        return Ut.sureJObject(CONFIGURATION.getJsonObject("commandsBack"));
    }
}
