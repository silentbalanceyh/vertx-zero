package io.vertx.tp.plugin.shell.refine;

import io.horizon.eon.em.Environment;
import io.vertx.tp.plugin.shell.atom.CommandAtom;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;

import java.util.List;
import java.util.function.Supplier;

/**
 * The tool class for Shell
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
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

    public static void welcome() {
        SlWelcome.welcome();
    }

    public static void goodbye() {
        SlWelcome.goodbye();
    }

    public static void goodbye(final CommandAtom option) {
        SlWelcome.goodbye(option);
    }

    public static void welcomeCommand(final Environment environment) {
        SlWelcome.welcomeCommand(environment);
    }

    public static void welcomeCommand(final CommandAtom option) {
        SlWelcome.welcomeCommand(option);
    }

    public static void welcomeSub(final Environment environment, final CommandAtom option) {
        SlWelcome.welcomeSub(environment, option);
    }

    /*
     * Workflow
     */
    public static void failEmpty() {
        SlMessage.failEmpty();
    }

    public static void failInvalid(final String command) {
        SlMessage.failInvalid(command);
    }

    public static void failWarn(final String message, final Object... args) {
        SlMessage.failWarn(message, args);
    }

    public static TermStatus failError(final Throwable ex) {
        return SlMessage.failError(ex);
    }


    /*
     * Message
     * - output, Print message information in console
     * - message, Format message information with dynamic `args`
     */
    public static void output(final String message, final Object... args) {
        SlLog.output(message, args);
    }

    public static void outputOpt(final String message, final String name, final Object... args) {
        SlLog.outputOpt(message, name, args);
    }

    public static String message(final String message, final Object... args) {
        return SlLog.message(message, args);
    }

    public static String message(final String key, final Supplier<String> defaultSupplier) {
        return SlMessage.message(key, defaultSupplier);
    }

    /*
     * Get Commands Here
     */
    public static List<CommandAtom> commands() {
        return SlCommand.commands();
    }

    public static List<CommandAtom> commands(final List<CommandAtom> commands) {
        return SlCommand.commands(commands);
    }
}
