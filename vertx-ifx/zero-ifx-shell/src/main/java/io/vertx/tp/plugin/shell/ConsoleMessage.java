package io.vertx.tp.plugin.shell;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.shell.atom.CommandOption;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class ConsoleMessage {

    private static final JsonObject WELCOME = Sl.welcome();

    static void welcome() {
        final String banner = WELCOME.getString("banner");
        final String version = WELCOME.getString("version");
        System.out.println("------------------------------------------------------");
        System.out.println("|                                                    |");
        System.out.println("|         " + banner + "         |");
        System.out.println("|                                                    |");
        System.out.println("------------------------------------------------------");
        System.out.println("                                   ---- Version." + version + "   ");
    }

    static void input(final Environment environment) {
        final JsonObject message = WELCOME.getJsonObject("message");
        Sl.output("------------->>>> Command Begin");
        Sl.output(message.getString("environment"), environment);
        Sl.output(message.getString("wait"));
        System.out.print(">> ");
    }

    static void input(final CommandOption option) {
        final JsonObject message = WELCOME.getJsonObject("message");
        Sl.output("------------( Sub-System )");
        Sl.output(message.getString("wait"));
        System.out.print(">> ");
    }

    static void failInput() {
        Sl.output(message("empty",
                () -> "Empty arguments here for redo"));
    }

    static void failError(final Throwable ex) {
        final String error = Objects.isNull(ex) ? "Error" : ex.getMessage();
        Sl.output(message("error",
                () -> "Error occurs {0}"), error);
    }

    public static void quit() {
        Sl.output(message("quit",
                /* Default supplier for "quit" */
                () -> "You have quit Zero Console successfully!"));
    }

    public static String header() {
        return message("header",
                /* Default supplier for "header" */
                () -> "Zero Framework Console/Shell!");
    }

    public static String footer() {
        return message("footer",
                /* Default supplier for "header" */
                () -> "CopyRight: http://www.vertxup.cn");
    }

    public static String usage() {
        return message("usage",
                /* Default supplier for "usage" */
                () -> "Syntax for different type:" +
                        "\t SYSTEM: <command> Go to sub-system of console." +
                        "\t COMMAND: <command> [options] Execute actual command" +
                        "\t Options: [ -name1 value1 -name2 value2 ]");
    }

    private static String message(final String key, final Supplier<String> defaultSupplier) {
        final JsonObject message = WELCOME.getJsonObject("message");
        final String information = message.getString(key);
        if (Ut.isNil(information)) {
            return defaultSupplier.get();
        } else {
            return information;
        }
    }
}
