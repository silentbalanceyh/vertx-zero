package io.vertx.tp.plugin.shell.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.shell.atom.CommandAtom;
import io.horizon.eon.em.Environment;
import io.vertx.up.log.Log;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SlWelcome {
    static final JsonObject WELCOME = SlConfig.welcome();

    static void welcome() {
        final String banner = WELCOME.getString("banner");
        final String version = WELCOME.getString("version");
        System.out.println("----------------------------------------------------------------");
        System.out.println("|                                                              |");
        System.out.println("|         " + Log.color(banner, Log.COLOR_CYAN, true) + "              |");
        System.out.println("|                                                              |");
        System.out.println("----------------------------------------------------------------");
        System.out.println("                                   ---- Version." + version + "   ");
    }

    static void welcomeCommand(final Environment environment) {
        final JsonObject message = WELCOME.getJsonObject("message");
        SlLog.output("------------->>>> " + Log.color("Command Begin", Log.COLOR_BLUE));
        SlLog.output(Log.color(message.getString("environment"), Log.COLOR_BLANK, true) + " " + message.getString("wait"), environment);
        System.out.print(">> ");
    }

    static void welcomeSub(final Environment environment, final CommandAtom option) {
        final JsonObject message = WELCOME.getJsonObject("message");
        SlLog.outputOpt("------>>>> " + Log.color("Sub System", Log.COLOR_GREEN) + ": {0}", option.getName(), option.getDescription());
        SlLog.output(Log.color(message.getString("environment"), Log.COLOR_BLANK, true) + " " + message.getString("wait"), environment);
        System.out.print(">> ");
    }

    static void welcomeCommand(final CommandAtom option) {
        SlLog.output(SlMessage.message("previous",
            () -> "Previous: name = {0}, description = {1}"), option.getSimple(), option.getDescription());
    }

    static void goodbye() {
        SlLog.output(SlMessage.message("quit",
            /* Default supplier for "quit" */
            () -> "You have quit Zero Console successfully!"));
    }

    static void goodbye(final CommandAtom option) {
        final String pattern = SlMessage.message("back",
            /* Default supplier for "quit" */
            () -> "You have quit current Sub System: {0} successfully!");
        SlLog.outputOpt(pattern,
            option.getName(), option.getDescription());
    }
}
