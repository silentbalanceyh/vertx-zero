package io.vertx.tp.workflow.refine;

import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class WfLog {

    static void info(final Annal logger,
                     final String flag, final String pattern, final Object... args) {
        logger.info(Log.blue("Ροή εργασίας") + " ( " + flag + " ) " + pattern, args);
    }

    static void debug(final Annal logger,
                      final String flag, final String pattern, final Object... args) {
        logger.debug(Log.blue("Ροή εργασίας") + " ( " + flag + " ) " + pattern, args);
    }
}
