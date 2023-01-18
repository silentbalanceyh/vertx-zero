package io.vertx.tp.tpl.refine;

import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

class TlLog {

    static void info(final Annal logger,
                     final String flag, final String pattern, final Object... args) {
        logger.info(Log.blue("Πρότυπο") + " ( " + flag + " ) " + pattern, args);
    }

    static void debug(final Annal logger,
                      final String flag, final String pattern, final Object... args) {
        logger.debug(Log.blue("Πρότυπο") + " ( " + flag + " ) " + pattern, args);
    }
}
