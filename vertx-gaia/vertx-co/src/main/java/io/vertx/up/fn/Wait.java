package io.vertx.up.fn;


import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.exception.ZeroRunException;
import io.vertx.up.log.Annal;

import java.util.function.Consumer;

final class Wait {
    private static final Annal LOGGER = Annal.get(Wait.class);

    private Wait() {
    }

    @SuppressWarnings("all")
    static <T> Future<T> then(final Object asyncResult, final Promise<T> promise, final Throwable error) {
        final AsyncResult<T> result = (AsyncResult<T>) asyncResult;
        if (result.succeeded()) {
            promise.complete(result.result());
        } else {
            promise.fail(error);
        }
        return promise.future();
    }

    static <T> Future<T> then(final Consumer<Promise<T>> consumer) {
        final Promise<T> promise = Promise.promise();
        consumer.accept(promise);
        return promise.future();
    }

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

    static void wrapper(final Annal logger, final ZeroActuator actuator) {
        try {
            actuator.execute();
        } catch (final ZeroException ex) {
            Annal.sure(logger, () -> logger.zero(ex));
            throw new ZeroRunException(ex.getMessage()) {
            };
        } catch (final Throwable ex) {
            Annal.sure(logger, () -> logger.jvm(ex));
        }
    }
}
