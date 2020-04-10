package io.vertx.tp.ui.refine;

import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

class UiLog {

    private static void info(final Annal logger,
                             final String flag, final String pattern, final Object... args) {
        logger.info(Log.blue("διεπαφή χρήστη") + " ( " + flag + " ) " + pattern, args);
    }

    private static void debug(final Annal logger,
                              final String flag, final String pattern, final Object... args) {
        logger.debug(Log.blue("διεπαφή χρήστη") + " ( " + flag + " ) " + pattern, args);
    }

    private static void error(final Annal logger,
                              final String flag, final String pattern, final Object... args) {
        logger.error(Log.blue("διεπαφή χρήστη") + " ( " + flag + " ) " + pattern, args);
    }

    static void infoInit(final Annal logger, final String pattern, final Object... args) {
        info(logger, "Init", pattern, args);
    }

    static void infoUi(final Annal logger, final String pattern, final Object... args) {
        info(logger, "UI", pattern, args);
    }

    static void infoWarn(final Annal logger, final String pattern, final Object... args) {
        error(logger, "Warn", pattern, args);
    }
}
