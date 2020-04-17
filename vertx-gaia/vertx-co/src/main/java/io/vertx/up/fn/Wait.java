package io.vertx.up.fn;


import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.up.fn.wait.Case;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

final class Wait {
    private Wait() {
    }

    static <T> void testing(final TestContext context, final Future<T> future, final Consumer<T> consumer) {
        final Async async = context.async();
        future.onComplete(handler -> {
            if (handler.succeeded()) {
                consumer.accept(handler.result());
            } else {
                final Throwable ex = handler.cause();
                if (Objects.nonNull(ex)) {
                    ex.printStackTrace();
                }
                context.fail(ex);
            }
            async.complete();
        });
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

    static <T> Case<T> branch(final Supplier<Future<T>> caseLine) {
        return Case.item(caseLine);
    }

    static <T> Case<T> branch(final Actuator executor, final Supplier<Future<T>> caseLine) {
        if (null != executor) {
            executor.execute();
        }
        return Case.item(caseLine);
    }

    static <T> Case<T> branch(final boolean condition, final Supplier<Future<T>> caseLine) {
        return Case.item(() -> condition, caseLine);
    }

    static <T> Case<T> branch(final boolean condition, final Actuator executor, final Supplier<Future<T>> caseLine) {
        if (condition) {
            if (null != executor) {
                executor.execute();
            }
        }
        return branch(condition, caseLine);
    }

    @SuppressWarnings("unchecked")
    static <T> Case<T> match(final Supplier<Case.DefaultCase<T>> defaultSupplier, final Case<T>... matchers) {
        for (final Case<T> each : matchers) {
            if (each.first.get()) {
                return each;
            }
        }
        return defaultSupplier.get();
    }
}
