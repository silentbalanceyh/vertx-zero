package io.vertx.tp.plugin.shell;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.eon.em.Environment;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class ConsoleWelcome {

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
        Sl.message(message.getString("environment"), environment);
        Sl.message(message.getString("wait"));
        System.out.print(">> ");
    }
}
