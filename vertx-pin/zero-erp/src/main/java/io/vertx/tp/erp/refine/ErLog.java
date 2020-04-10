package io.vertx.tp.erp.refine;

import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

class ErLog {

    private static void info(final Annal logger,
                             final String flag, final String pattern, final Object... args) {
        logger.info(Log.blue("Επιχείρηση") + " ( " + flag + " ) " + pattern, args);
    }

    private static void debug(final Annal logger,
                              final String flag, final String pattern, final Object... args) {
        logger.debug(Log.blue("Επιχείρηση") + " ( " + flag + " ) " + pattern, args);
    }

    static void infoWorker(final Annal logger, final String pattern, final Object... args) {
        info(logger, "Worker", pattern, args);
    }
}
