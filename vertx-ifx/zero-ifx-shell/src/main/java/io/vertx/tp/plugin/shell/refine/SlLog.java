package io.vertx.tp.plugin.shell.refine;

import java.text.MessageFormat;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class SlLog {

    static void output(final String message, final Object... args) {
        stream(message, "[ μηδέν ] ", args);
    }

    static void outputOpt(final String message, final String name, final Object... args) {
        stream(message, "[ μηδέν ] (" + name + ")", args);
    }

    private static void stream(final String message, final String flag, final Object... args) {
        if (0 == args.length) {
            System.out.println(flag + message);
        } else {
            System.out.println(MessageFormat.format(flag + message, args));
        }
    }

    static String message(final String message, final Object... args) {
        if (0 == args.length) {
            return ("[ μηδέν ] " + message);
        } else {
            return (MessageFormat.format("[ μηδέν ] " + message, args));
        }
    }
}
