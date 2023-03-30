package io.vertx.tp.battery.refine;

import io.vertx.up.log.Annal;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Bk {

    public static class Log {
        public static void infoInit(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            BkLog.info(logger, "Init", message, args);
        }

        public static void warnInit(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            BkLog.warn(logger, "Init", message, args);
        }

        public static void infoChannel(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            BkLog.info(logger, "Service Loader", message, args);
        }
    }
}
