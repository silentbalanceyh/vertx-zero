package io.vertx.up.fn;

import io.vertx.core.Future;
import io.vertx.up.log.Annal;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * Wrapper processing
 */
class Wrapper {
    private static final Annal LOGGER = Annal.get(Wrapper.class);

    /*
     * JvmSupplier -> Supplier
     */
    static <T> T wrapper(final RunSupplier<T> supplier, final T defaultValue) {
        try {
            return supplier.get();
        } catch (final Throwable ex) {
            // TODO: For Debug
            LOGGER.jvm(ex);
            ex.printStackTrace();
            return defaultValue;
        }
    }

    static <T> Future<T> wrapperAsync(final RunSupplier<Future<T>> supplier, final T defaultValue) {
        try {
            return supplier.get();
        } catch (final Throwable ex) {
            // TODO: For Debug
            LOGGER.jvm(ex);
            ex.printStackTrace();
            return Future.succeededFuture(defaultValue);
        }
    }
}
