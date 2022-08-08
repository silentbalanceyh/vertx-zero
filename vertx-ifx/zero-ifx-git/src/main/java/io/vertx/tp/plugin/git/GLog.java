package io.vertx.tp.plugin.git;

import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class GLog {
    private static void info(final Class<?> clazz, final String flag, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        logger.info(Log.blue("πηγή") + " ( " + flag + " ) " + pattern, args);
    }

    private static void debug(final Class<?> clazz, final String flag, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        logger.debug(Log.blue("πηγή") + " ( " + flag + " ) " + pattern, args);
    }

    private static void warn(final Class<?> clazz, final String flag, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        logger.warn(Log.blue("πηγή") + " ( " + flag + " ) " + pattern, args);
    }

    static void infoRepo(final Class<?> clazz, final String pattern, final Object... args) {
        info(clazz, "Repo", pattern, args);
    }

    static void infoCommand(final Class<?> clazz, final String pattern, final Object... args) {
        info(clazz, "Git", pattern, args);
    }
}
