package io.vertx.tp.plugin.shell.refine;

import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SlLog {

    static void output(final String message, final Object... args) {
        stream(message, Ut.flagBBlue("[ μηδέν ]"), args);
    }

    static void outputOpt(final String message, final String name, final Object... args) {
        stream(message, Ut.flagBBlue("[ μηδέν ]") + " (" + name + ")", args);
    }

    private static void stream(final String message, final String flag, final Object... args) {
        if (0 == args.length) {
            System.out.println(flag + message);
        } else {
            System.out.println(Ut.fromMessage(flag + message, args));
        }
    }

    static String message(final String message, final Object... args) {
        if (0 == args.length) {
            return ("[ μηδέν ] " + message);
        } else {
            return (Ut.fromMessage("[ μηδέν ] " + message, args));
        }
    }
}
