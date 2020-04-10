package io.vertx.tp.ke.refine;

import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

class KeLog {
    private static void info(final Annal logger,
                             final String flag, final String pattern, final Object... args) {
        logger.info(Log.blue("Εισόδημα") + " ( " + flag + " ) " + pattern, args);
    }

    private static void warn(final Annal logger,
                             final String flag, final String pattern, final Object... args) {
        logger.warn(Log.blue("Εισόδημα") + " ( " + flag + " ) " + pattern, args);
    }

    private static void debug(final Annal logger,
                              final String flag, final String pattern, final Object... args) {
        logger.debug(Log.blue("Εισόδημα") + " ( " + flag + " ) " + pattern, args);
    }

    static void infoKe(final Annal logger, final String pattern, final Object... args) {
        info(logger, "Ke", pattern, args);
    }


    static void debugKe(final Annal logger, final String pattern, final Object... args) {
        debug(logger, "Ke", pattern, args);
    }

    static void debugChannel(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        debug(logger, "Channel", pattern, args);
    }

    static void infoChannel(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        info(logger, "Channel", pattern, args);
    }

    static void warnChannel(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        warn(logger, "Channel", pattern, args);
    }
}
