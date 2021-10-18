package io.vertx.tp.plugin.shell.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.Infix;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SlConfig {

    private static final JsonObject CONFIGURATION = new JsonObject();
    private static final String FIELD_COMMANDS = "commands";
    private static final String FIELD_DEFAULT = "default";
    private static final String FIELD_DEFINED = "defined";

    static void init() {
        /*
         * Initialize client
         */
        final JsonObject data = Infix.init("shell", config -> {
            /*
             * command configuration
             */
            final JsonObject commands = Ut.sureJObject(config.getJsonObject(FIELD_COMMANDS));
            /*
             * default, defined
             */
            final JsonObject commandsJson = new JsonObject();
            final String defaultFile = commands.getString(FIELD_DEFAULT);
            if (Ut.notNil(defaultFile)) {
                commandsJson.put(FIELD_DEFAULT, Ut.ioJArray(defaultFile));
            }
            final String definedFile = commands.getString(FIELD_DEFINED);
            if (Ut.notNil(definedFile)) {
                commandsJson.put(FIELD_DEFINED, Ut.ioJArray(definedFile));
            }
            config.put(FIELD_COMMANDS, commandsJson);
            return config;
        }, JsonObject.class);
        CONFIGURATION.mergeIn(data, true);
    }

    static JsonObject welcome() {
        return Ut.sureJObject(CONFIGURATION.getJsonObject("welcome"));
    }

    static boolean isDebug() {
        Boolean debug = Boolean.FALSE;
        if (CONFIGURATION.containsKey("debug")) {
            debug = CONFIGURATION.getBoolean("debug");
        }
        return debug;
    }

    static JsonObject validate() {
        return Ut.sureJObject(CONFIGURATION.getJsonObject("validate"));
    }

    static JsonArray commands() {
        final JsonObject commands = CONFIGURATION.getJsonObject(FIELD_COMMANDS);
        return Ut.sureJArray(commands.getJsonArray(FIELD_DEFINED));
    }

    static JsonArray commandsDefault() {
        final JsonObject commands = CONFIGURATION.getJsonObject(FIELD_COMMANDS);
        return Ut.sureJArray(commands.getJsonArray(FIELD_DEFAULT));
    }
}
