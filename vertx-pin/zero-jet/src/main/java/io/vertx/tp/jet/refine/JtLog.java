package io.vertx.tp.jet.refine;

import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

class JtLog {

    static void info(final Annal logger,
                     final String flag, final String pattern, final Object... args) {
        logger.info(Log.blue("Πίδακας δρομολογητή") + " ( " + flag + " ) " + pattern, args);
    }

    static void debug(final Annal logger,
                      final String flag, final String pattern, final Object... args) {
        logger.debug(Log.blue("Πίδακας δρομολογητή") + " ( " + flag + " ) " + pattern, args);
    }

    static void error(final Annal logger,
                      final String flag, final String pattern, final Object... args) {
        logger.error(Log.blue("Πίδακας δρομολογητή") + " ( " + flag + " ) " + pattern, args);
    }

    static void warn(final Annal logger,
                     final String flag, final String pattern, final Object... args) {
        logger.warn(Log.blue("Πίδακας δρομολογητή") + " ( " + flag + " ) " + pattern, args);
    }
}
