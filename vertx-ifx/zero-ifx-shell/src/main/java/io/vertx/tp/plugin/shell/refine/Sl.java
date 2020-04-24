package io.vertx.tp.plugin.shell.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.shell.atom.CommandOption;

import java.util.List;

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
     * Message
     * - output, Print message information in console
     * - message, Format message information with dynamic `args`
     */
    public static void output(final String message, final Object... args) {
        SlLog.output(message, args);
    }

    public static void outputSubSystem(final String message, final String name, final Object... args) {
        SlLog.output(message, name, args);
    }

    public static String message(final String message, final Object... args) {
        return SlLog.message(message, args);
    }

    /*
     * Execution life cycle
     */
    public static JsonObject welcome() {
        return SlConfig.welcome();
    }

    /*
     * Get Commands Here
     */
    public static List<CommandOption> commands() {
        return SlCommand.commands();
    }
}
