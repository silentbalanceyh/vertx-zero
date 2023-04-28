package io.horizon.fn;

import io.horizon.exception.AbstractException;
import io.horizon.util.HaS;

import java.util.function.Supplier;

/**
 * @author lang : 2023/4/28
 */
class HThrow {

    static <T> T runOut(final Supplier<T> supplier, final Class<? extends AbstractException> runCls, final Object... args) {
        T ret = null;
        try {
            ret = supplier.get();
        } catch (final Throwable ex) {
            final Object[] argument = HaS.elementAdd(args, ex);
            final AbstractException error = HaS.instance(runCls, argument);
            if (null != error) {
                throw error;
            }
        }
        return ret;
    }
}
