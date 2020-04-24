package io.vertx.tp.plugin.shell.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * The tool class for Shell
 *
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class Sl {
    /*
     * Initialize the console life cycle
     */
    public static void init() {
        /* Configuration initialize */
        SlConfig.init();
    }

    /*
     * Validate life cycle
     */
    public static boolean ready(final String[] args) {
        /* Validation for string */
        return SlVerifier.validate(args);
    }

    /*
     * Message output
     */
    public static void message(final String message, final Object... args) {
        SlLog.message(message, args);
    }

    /*
     * Execution life cycle
     */
    public static JsonObject welcome() {
        return SlConfig.welcome();
    }

    public static JsonArray commands(final boolean isDefault) {
        return isDefault ? SlConfig.commandsDefault() : SlConfig.commands();
    }
}
