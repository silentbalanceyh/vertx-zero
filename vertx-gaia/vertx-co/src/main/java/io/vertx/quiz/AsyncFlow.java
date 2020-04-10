package io.vertx.quiz;

import io.vertx.core.Future;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

import java.util.function.Consumer;

public class AsyncFlow {

    public static <T> void async(final TestContext context,
                                 final Future<T> future,
                                 final Consumer<T> consumer) {
        final Async async = context.async();
        future.setHandler(handler -> {
            if (handler.succeeded()) {
                consumer.accept(handler.result());
            } else {
                handler.cause().printStackTrace();
                context.fail(handler.cause());
            }
            async.complete();
        });
    }
}
