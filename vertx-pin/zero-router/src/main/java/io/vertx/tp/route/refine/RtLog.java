package io.vertx.tp.route.refine;

import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

class RtLog {
    private static void info(final Annal logger,
                             final String flag, final String pattern, final Object... args) {
        logger.info(Log.blue("πύλη") + " ( " + flag + " ) " + pattern, args);
    }

    static void infoInit(final Annal logger, final String pattern, final Object... args) {
        info(logger, "Init", pattern, args);
    }
}
