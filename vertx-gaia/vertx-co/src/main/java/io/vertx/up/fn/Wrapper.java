package io.vertx.up.fn;

import io.vertx.core.Future;
import io.vertx.up.log.Annal;

import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Wrapper processing
 */
class Wrapper {
    private static final Annal LOGGER = Annal.get(Wrapper.class);

    /*
     * JvmSupplier -> Supplier
     */
    static <T> Supplier<T> wrapper(final RunSupplier<T> supplier, final T defaultValue) {
        return () -> {
            try {
                return supplier.get();
            } catch (final Throwable ex) {
                LOGGER.jvm(ex);
                return defaultValue;
            }
        };
    }

    static <T> Supplier<Future<T>> wrapperAsync(final RunSupplier<Future<T>> supplier, final T defaultValue) {
        return () -> {
            try {
                return supplier.get();
            } catch (final Throwable ex) {
                LOGGER.jvm(ex);
                return Future.succeededFuture(defaultValue);
            }
        };
    }
}
