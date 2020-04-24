package io.vertx.tp.plugin.shell.refine;

import java.text.MessageFormat;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class SlLog {

    static void message(final String message, final Object... args) {
        if (0 == args.length) {
            System.out.println("[ μηδέν ] " + message);
        } else {
            System.out.println(MessageFormat.format("[ μηδέν ] " + message, args));
        }
    }
}
