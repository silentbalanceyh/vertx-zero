package io.vertx.up.util;

import io.vertx.aeon.eon.em.TypeOs;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class Env {

    static boolean envBool(final String name) {
        final String ioDebug = System.getenv(name);
        return "true".equalsIgnoreCase(ioDebug);
    }

    static void execSingle(final String command, final TypeOs os) {
        if (TypeOs.WINDOWS == os) {
            // Windows Pending
            Fn.safeJvm(() -> Runtime.getRuntime().exec(command));
        } else {
            // Linux Unix
            Fn.safeJvm(() -> Runtime.getRuntime().exec(command));
        }
    }
}
