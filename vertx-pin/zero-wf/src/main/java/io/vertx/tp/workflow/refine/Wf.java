package io.vertx.tp.workflow.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Wf {

    public static JsonObject argsForm(final JsonObject form, final String sigma) {
        return WfForm.argsForm(form, sigma);
    }

    public static class Log {
        public static void infoInit(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.info(logger, "Init", message, args);
        }

        public static void debugInit(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.debug(logger, "Init", message, args);
        }

        public static void infoDeploy(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.info(logger, "Deploy", message, args);
        }

        public static void warnDeploy(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.warn(logger, "Deploy", message, args);
        }

        public static void debugDeploy(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.debug(logger, "Deploy", message, args);
        }

        public static void infoMove(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.info(logger, "Move", message, args);
        }

        public static void debugMove(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            WfLog.debug(logger, "Move", message, args);
        }
    }
}
