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
        Fn.safeJvm(() -> {
            if (TypeOs.MAC_OS == os) {
                final Runtime runtime = Runtime.getRuntime();
                runtime.exec(command);
            }
        });

    }
}
