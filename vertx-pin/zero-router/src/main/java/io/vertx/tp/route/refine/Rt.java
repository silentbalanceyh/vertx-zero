package io.vertx.tp.route.refine;

import io.vertx.up.log.Annal;

public final class Rt {
    /*
     * Log information of gateway
     */
    public static void infoInit(final Annal logger, final String pattern, final Object... args) {
        RtLog.infoInit(logger, pattern, args);
    }
}
