package io.vertx.up.fn;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;

import java.util.function.Consumer;

/**
 * @author lang : 2023/4/28
 */
class Then {
    private Then() {

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
}
