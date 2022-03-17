package io.vertx.tp.is.refine;

import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IsLog {

    static void info(final Annal logger,
                     final String flag, final String pattern, final Object... args) {
        logger.info(Log.blue("Ολοκλήρωση") + " ( " + flag + " ) " + pattern, args);
    }

    static void warn(final Annal logger,
                     final String flag, final String pattern, final Object... args) {
        logger.warn(Log.blue("Ολοκλήρωση") + " ( " + flag + " ) " + pattern, args);
    }

    static void debug(final Annal logger,
                      final String flag, final String pattern, final Object... args) {
        logger.debug(Log.blue("Ολοκλήρωση") + " ( " + flag + " ) " + pattern, args);
    }
}
