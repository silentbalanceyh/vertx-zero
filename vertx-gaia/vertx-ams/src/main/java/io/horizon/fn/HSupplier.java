package io.horizon.fn;

import io.horizon.exception.AbstractException;
import io.horizon.exception.ProgramException;
import io.horizon.specification.uca.HLogger;
import io.horizon.util.HUt;

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

    static <T> T failOr(final Supplier<T> supplier,
                        final Class<? extends AbstractException> runCls, final Object... args) {
        T ret = null;
        try {
            ret = supplier.get();
        } catch (final Throwable ex) {
            final Object[] argument = HUt.elementAdd(args, ex);
            final AbstractException error = HUt.instance(runCls, argument);
            if (null != error) {
                throw error;
            }
        }
        return ret;
    }

    public static <T> T bugOr(final T defaultValue, final ProgramSupplier<T> supplier,
                              final HLogger logger) throws ProgramException {
        return HFunction.bugAt(defaultValue, (t) -> supplier.get(), logger);
    }

    public static <T> T runOr(final T defaultValue, final Supplier<T> supplier) {
        return HFunction.runAt(defaultValue, (t) -> supplier.get());
    }
}
