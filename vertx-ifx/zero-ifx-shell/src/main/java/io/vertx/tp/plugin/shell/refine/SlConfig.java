package io.vertx.tp.plugin.shell.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.Infix;
import io.vertx.up.util.Ut;

import java.util.Set;

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

    static JsonObject readyInput() {
        return Ut.sureJObject(readyValidate().getJsonObject("input"));
    }

    static Set<String> readyArgs() {
        final JsonArray args = Ut.sureJArray(readyValidate().getJsonArray("args"));
        return Ut.toSet(args);
    }

    private static JsonObject readyValidate() {
        return Ut.sureJObject(CONFIGURATION.getJsonObject("validate"));
    }
}
