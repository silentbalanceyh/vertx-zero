package io.vertx.tp.jet.refine;

import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

class JtLog {

    private static void info(final Annal logger,
                             final String flag, final String pattern, final Object... args) {
        logger.info(Log.blue("Πίδακας δρομολογητή") + " ( " + flag + " ) " + pattern, args);
    }

    private static void debug(final Annal logger,
                              final String flag, final String pattern, final Object... args) {
        logger.debug(Log.blue("Πίδακας δρομολογητή") + " ( " + flag + " ) " + pattern, args);
    }

    private static void error(final Annal logger,
                              final String flag, final String pattern, final Object... args) {
        logger.error(Log.blue("Πίδακας δρομολογητή") + " ( " + flag + " ) " + pattern, args);
    }

    static void infoInit(final Annal logger, final String pattern, final Object... args) {
        info(logger, "Init", pattern, args);
    }


    static void infoRoute(final Annal logger, final String pattern, final Object... args) {
        info(logger, "Route", pattern, args);
    }

    static void infoWeb(final Annal logger, final String pattern, final Object... args) {
        info(logger, "Web", pattern, args);
    }

    static void infoWorker(final Annal logger, final String pattern, final Object... args) {
        info(logger, "Worker", pattern, args);
    }
}
