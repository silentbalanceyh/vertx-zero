package io.vertx.tp.plugin.neo4j.refine;

import io.vertx.up.log.Annal;

class N4JLog {

    static void info(final Annal logger, final String flag, final String pattern, final Object... args) {
        logger.info("[ γραφικό ] ( " + flag + " ) " + pattern, args);
    }

    static void debug(final Annal logger, final String flag, final String pattern, final Object... args) {
        logger.debug("[ γραφικό ] ( " + flag + " ) " + pattern, args);
    }

    static void warn(final Annal logger, final String flag, final String pattern, final Object... args) {
        logger.warn("[ γραφικό ] ( " + flag + " ) " + pattern, args);
    }
}
