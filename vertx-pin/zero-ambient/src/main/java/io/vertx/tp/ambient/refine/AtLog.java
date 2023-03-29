package io.vertx.tp.ambient.refine;

import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

class AtLog {

    static void info(final Annal logger,
                     final String flag, final String pattern, final Object... args) {
        logger.info(Log.blue("περιβάλλων") + " ( " + flag + " ) " + pattern, args);
    }
    static void warn(final Annal logger,
                     final String flag, final String pattern, final Object... args) {
        logger.warn(Log.blue("περιβάλλων") + " ( " + flag + " ) " + pattern, args);
    }
    static void debug(final Annal logger,
                      final String flag, final String pattern, final Object... args) {
        logger.debug(Log.blue("περιβάλλων") + " ( " + flag + " ) " + pattern, args);
    }

    static void infoEnv(final Annal logger, final String pattern, final Object... args) {
        AtLog.info(logger, "Env", pattern, args);
    }
}
