package io.vertx.up.util;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class Env {

    static boolean envBool(final String name) {
        final String ioDebug = System.getenv(name);
        return "true".equalsIgnoreCase(ioDebug);
    }
}
