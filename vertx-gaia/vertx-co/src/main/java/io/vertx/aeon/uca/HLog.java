package io.vertx.aeon.uca;

import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HLog {

    private static void info(final Class<?> clazz, final String flag, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        logger.info(Log.blue("Δέκα δισεκατομμύρια") + " ( " + flag + " ) " + pattern, args);
    }

    private static void debug(final Class<?> clazz, final String flag, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        logger.debug(Log.blue("Δέκα δισεκατομμύρια") + " ( " + flag + " ) " + pattern, args);
    }

    public static void infoUp(final Class<?> clazz, final String pattern, final Object... args) {
        info(clazz, "Up", pattern, args);
    }

    public static void infoK8(final Class<?> clazz, final String pattern, final Object... args) {
        info(clazz, "K8S", pattern, args);
    }
}
