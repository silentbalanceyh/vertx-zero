package io.vertx.up.fn;


import io.horizon.exception.ProgramException;
import io.horizon.fn.ErrorSupplier;
import io.horizon.fn.ProgramActuator;
import io.vertx.core.Future;
import io.vertx.up.log.Annal;

final class Wait {
    private static final Annal LOGGER = Annal.get(Wait.class);

    private Wait() {
    }


    /*
     * JvmSupplier -> Supplier
     */
    static <T> T wrapper(final ErrorSupplier<T> supplier, final T defaultValue) {
        try {
            return supplier.get();
        } catch (final Throwable ex) {
            // TODO: For Debug
            LOGGER.fatal(ex);
            ex.printStackTrace();
            return defaultValue;
        }
    }

    static <T> Future<T> wrapperAsync(final ErrorSupplier<Future<T>> supplier, final T defaultValue) {
        try {
            return supplier.get();
        } catch (final Throwable ex) {
            // TODO: For Debug
            LOGGER.fatal(ex);
            ex.printStackTrace();
            return Future.succeededFuture(defaultValue);
        }
    }

    static void wrapper(final Annal logger, final ProgramActuator actuator) {
        try {
            actuator.execute();
        } catch (final ProgramException ex) {
            logger.fatal(ex);
            //            Annal.?ure(logger, () -> logger.checked(ex));
            //            throw new AbstractException(ex.getMessage()) {
            //            };
        } catch (final Throwable ex) {
            logger.fatal(ex);
            //            Annal.?ure(logger, () -> logger.jvm(ex));
        }
    }
}
