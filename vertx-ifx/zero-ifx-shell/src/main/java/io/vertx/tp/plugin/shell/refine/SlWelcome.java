package io.vertx.tp.plugin.shell.refine;

import io.horizon.eon.em.Environment;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.shell.atom.CommandAtom;
import io.vertx.up.util.Ut;

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
        System.out.println("|         " + Ut.rgbCyanB(banner) + "              |");
        System.out.println("|                                                              |");
        System.out.println("----------------------------------------------------------------");
        System.out.println("                                   ---- Version." + version + "   ");
    }

    static void welcomeCommand(final Environment environment) {
        final JsonObject message = WELCOME.getJsonObject("message");
        SlMessage.output("------------->>>> " + Ut.rgbBlueN("Command Begin"));
        SlMessage.output(Ut.rgbBlankB(message.getString("environment")) + " " + message.getString("wait"), environment);
        System.out.print(">> ");
    }

    static void welcomeSub(final Environment environment, final CommandAtom option) {
        final JsonObject message = WELCOME.getJsonObject("message");
        SlMessage.outputOpt("------>>>> " + Ut.rgbGreenN("Sub System") + ": {0}", option.getName(), option.getDescription());
        SlMessage.output(Ut.rgbBlankB(message.getString("environment")) + " " + message.getString("wait"), environment);
        System.out.print(">> ");
    }

    static void welcomeCommand(final CommandAtom option) {
        SlMessage.output(SlMessage.message("previous",
            () -> "Previous: name = {0}, description = {1}"), option.getSimple(), option.getDescription());
    }

    static void goodbye() {
        SlMessage.output(SlMessage.message("quit",
            /* Default supplier for "quit" */
            () -> "You have quit Zero Console successfully!"));
    }

    static void goodbye(final CommandAtom option) {
        final String pattern = SlMessage.message("back",
            /* Default supplier for "quit" */
            () -> "You have quit current Sub System: {0} successfully!");
        SlMessage.outputOpt(pattern,
            option.getName(), option.getDescription());
    }
}
