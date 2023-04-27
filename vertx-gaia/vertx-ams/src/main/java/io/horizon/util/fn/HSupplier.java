package io.horizon.util.fn;

import io.horizon.fn.ErrorSupplier;
import io.horizon.fn.ExceptionSupplier;
import io.horizon.fn.ProgramSupplier;
import io.horizon.log.HLogger;
import io.horizon.util.HaS;

/**
 * @author lang : 2023/4/28
 */
class HSupplier {
    private HSupplier() {
    }

    public static <T> T jvmNOr(final T defaultValue, final ErrorSupplier<T> supplier,
                               final Object... input) {
        if (HaS.isNotNull(input)) {
            return jvmOr(defaultValue, supplier, null);
        } else {
            return defaultValue;
        }
    }

    public static <T> T jvmOr(final T defaultValue, final ErrorSupplier<T> supplier,
                              final HLogger logger) {
        return HFunction.jvmAt(defaultValue, (t) -> supplier.get(), logger);
    }

    public static <T> T failNOr(final T defaultValue, final ExceptionSupplier<T> supplier,
                                final Object... input) {
        if (HaS.isNotNull(input)) {
            return failOr(defaultValue, supplier, null);
        } else {
            return defaultValue;
        }
    }

    public static <T> T failOr(final T defaultValue, final ExceptionSupplier<T> supplier,
                               final HLogger logger) {
        return HFunction.failAt(defaultValue, (t) -> supplier.get(), logger);
    }

    public static <T> T bugOr(final T defaultValue, final ProgramSupplier<T> supplier,
                              final HLogger logger) {
        return HFunction.bugAt(defaultValue, (t) -> supplier.get(), logger);
    }
}
