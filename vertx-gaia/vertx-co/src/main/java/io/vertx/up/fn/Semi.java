package io.vertx.up.fn;

import io.vertx.up.log.Annal;
import io.vertx.up.exception.ZeroException;

import java.util.function.Supplier;

/**
 * Semi is for if statement, once the condition
 * is checked, the code continue to execute
 */
final class Semi {

    private Semi() {
    }

    static <T> T execReturn(final Supplier<T> supplier, final T defaultValue) {
        final T ret = supplier.get();
        return null == ret ? defaultValue : ret;
    }

    static void exec(final boolean condition, final Annal logger, final Actuator tSupplier, final Actuator fSupplier) {
        Defend.zeroVoid(() -> execZero(condition,
                () -> {
                    if (null != tSupplier) {
                        tSupplier.execute();
                    }
                    return null;
                }, () -> {
                    if (null != fSupplier) {
                        fSupplier.execute();
                    }
                    return null;
                }), logger);
    }

    static <T> T execZero(final boolean condition, final ZeroSupplier<T> tSupplier, final ZeroSupplier<T> fSupplier)
            throws ZeroException {
        T ret = null;
        if (condition) {
            if (null != tSupplier) {
                ret = tSupplier.get();
            }
        } else {
            if (null != fSupplier) {
                ret = fSupplier.get();
            }
        }
        return ret;
    }
}
