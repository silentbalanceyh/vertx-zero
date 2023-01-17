package io.vertx.tp.tpl.refine;

import io.vertx.up.log.Annal;

/*
 * Tool class available in current service only
 */
public class Tl {

    public static void infoFlow(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        TlLog.info(logger, "Execution", pattern, args);
    }
}
