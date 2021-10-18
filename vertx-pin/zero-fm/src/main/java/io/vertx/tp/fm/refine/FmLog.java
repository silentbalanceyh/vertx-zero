package io.vertx.tp.fm.refine;

import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class FmLog {
    static void info(final Class<?> clazz,
                     final String flag, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        logger.info(Log.blue("χρηματοδότηση") + " ( " + flag + " ) " + pattern, args);
    }

    static void warn(final Class<?> clazz,
                     final String flag, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        logger.warn(Log.blue("χρηματοδότηση") + " ( " + flag + " ) " + pattern, args);
    }
}
