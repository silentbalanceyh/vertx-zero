package io.vertx.up.log;

import io.horizon.exception.ZeroException;
import io.horizon.exception.ZeroRunException;
import io.horizon.fn.Actuator;
import io.vertx.up.log.internal.BridgeAnnal;

/**
 * Unite Logging system connect to vert.x, io.vertx.zero.io.vertx.zero.io.vertx.up.io.vertx.up.io.vertx.up.util kit of Vertx-Zero
 */
public interface Annal {

    static Annal get(final Class<?> clazz) {
        return new BridgeAnnal(clazz);
    }

    /*
     * Re-invoked logging for executing, here are logger sure to
     * Avoid Null Pointer exception
     */
    static <T> void sure(final Annal logger, final Actuator actuator) {
        if (null != logger) {
            actuator.execute();
        }
    }

    void runtime(ZeroRunException ex);

    void checked(ZeroException ex);

    void jvm(Throwable ex);

    void warn(String key, Object... args);

    void error(String key, Object... args);

    void info(String key, Object... args);

    void info(boolean condition, String key, Object... args);

    void debug(String key, Object... args);
}
