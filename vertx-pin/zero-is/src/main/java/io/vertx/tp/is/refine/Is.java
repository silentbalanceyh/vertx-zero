package io.vertx.tp.is.refine;

import io.vertx.up.log.Annal;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Is {
    public static class Log {

        public static void infoInit(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IsLog.info(logger, "Init", message, args);
        }

        public static void infoWeb(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IsLog.info(logger, "Web", message, args);
        }

        public static void warnPath(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IsLog.warn(logger, "Path", message, args);
        }
    }
}
