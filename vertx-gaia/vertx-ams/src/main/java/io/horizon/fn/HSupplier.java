package io.horizon.fn;

import io.horizon.exception.ProgramException;
import io.horizon.log.HLogger;

import java.util.function.Supplier;

/**
 * @author lang : 2023/4/28
 */
class HSupplier {
    private HSupplier() {
    }

    public static <T> T jvmOr(final T defaultValue, final ErrorSupplier<T> supplier,
                              final HLogger logger) {
        return HFunction.jvmAt(defaultValue, (t) -> supplier.get(), logger);
    }

    public static <T> T failOr(final T defaultValue, final ExceptionSupplier<T> supplier,
                               final HLogger logger) {
        return HFunction.failAt(defaultValue, (t) -> supplier.get(), logger);
    }

    public static <T> T bugOr(final T defaultValue, final ProgramSupplier<T> supplier,
                              final HLogger logger) throws ProgramException {
        return HFunction.bugAt(defaultValue, (t) -> supplier.get(), logger);
    }

    public static <T> T runOr(final T defaultValue, final Supplier<T> supplier) {
        return HFunction.runAt(defaultValue, (t) -> supplier.get());
    }
}
