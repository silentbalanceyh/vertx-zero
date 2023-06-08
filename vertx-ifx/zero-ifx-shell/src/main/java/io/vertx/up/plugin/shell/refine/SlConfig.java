package io.vertx.up.plugin.shell.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.plugin.Infix;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SlConfig {

    private static final JsonObject CONFIGURATION = new JsonObject();

    static void init() {
        /*
         * Initialize client
         */
        final JsonObject data = Infix.init(YmlCore.shell.__KEY, config -> {
            /*
             * command configuration
             */
            final JsonObject commands = Ut.valueJObject(config.getJsonObject(YmlCore.shell.COMMANDS));
            /*
             * default, defined
             */
            final JsonObject commandsJson = new JsonObject();
            final String defaultFile = commands.getString(YmlCore.shell.commands.DEFAULT);
            if (Ut.isNotNil(defaultFile)) {
                commandsJson.put(YmlCore.shell.commands.DEFAULT, Ut.ioJArray(defaultFile));
            }
            final String definedFile = commands.getString(YmlCore.shell.commands.DEFINED);
            if (Ut.isNotNil(definedFile)) {
                commandsJson.put(YmlCore.shell.commands.DEFINED, Ut.ioJArray(definedFile));
            }
            config.put(YmlCore.shell.COMMANDS, commandsJson);
            return config;
        }, JsonObject.class);
        CONFIGURATION.mergeIn(data, true);
    }

    static JsonObject welcome() {
        return Ut.valueJObject(CONFIGURATION.getJsonObject(YmlCore.shell.WELCOME));
    }

    static boolean isDebug() {
        Boolean debug = Boolean.FALSE;
        if (CONFIGURATION.containsKey(YmlCore.shell.DEBUG)) {
            debug = CONFIGURATION.getBoolean(YmlCore.shell.DEBUG);
        }
        return debug;
    }

    static JsonObject validate() {
        return Ut.valueJObject(CONFIGURATION.getJsonObject(YmlCore.shell.VALIDATE));
    }

    static JsonArray commands() {
        final JsonObject commands = CONFIGURATION.getJsonObject(YmlCore.shell.COMMANDS);
        return Ut.valueJArray(commands.getJsonArray(YmlCore.shell.commands.DEFINED));
    }

    static JsonArray commandsDefault() {
        final JsonObject commands = CONFIGURATION.getJsonObject(YmlCore.shell.COMMANDS);
        return Ut.valueJArray(commands.getJsonArray(YmlCore.shell.commands.DEFAULT));
    }
}
