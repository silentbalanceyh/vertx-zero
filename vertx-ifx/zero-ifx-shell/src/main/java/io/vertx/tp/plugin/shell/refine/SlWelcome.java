package io.vertx.tp.plugin.shell.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.shell.atom.CommandOption;
import io.vertx.up.eon.em.Environment;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class SlWelcome {
    static final JsonObject WELCOME = SlConfig.welcome();

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

    static void welcomeCommand(final Environment environment) {
        final JsonObject message = WELCOME.getJsonObject("message");
        SlLog.output("------------->>>> Command Begin");
        SlLog.output(message.getString("environment"), environment);
        SlLog.output(message.getString("wait"));
        System.out.print(">> ");
    }

    static void welcomeSub(final CommandOption option) {
        final JsonObject message = WELCOME.getJsonObject("message");
        SlLog.output("------------( Sub-System )");
        SlLog.output(message.getString("wait"));
        System.out.print(">> ");
    }

    static void goodbye() {
        SlLog.output(SlMessage.message("quit",
                /* Default supplier for "quit" */
                () -> "You have quit Zero Console successfully!"));
    }
}
