package io.vertx.up.unity;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

class Async {

    private static final Annal LOGGER = Annal.get(Async.class);

    @SuppressWarnings("all")
    static <T> Future<T> future(final T input, final List<Function<T, Future<T>>> queues) {
        if (0 == queues.size()) {
            /*
             * None queue here
             */
            return To.future(input);
        } else {
            Future<T> first = queues.get(Values.IDX).apply(input);
            if (Objects.isNull(first)) {
                LOGGER.error("The index = 0 future<T> returned null, plugins will be terminal");
                return To.future(input);
            } else {
                if (1 == queues.size()) {
                    /*
                     * Get first future
                     */
                    return first;
                } else {
                    /*
                     * future[0]
                     *    .compose(future[1])
                     *    .compose(future[2])
                     *    .compose(...)
                     */
                    final Refer response = new Refer();
                    response.add(input);

                    for (int idx = 1; idx < queues.size(); idx++) {
                        final int current = idx;
                        first = first.compose(json -> {
                            final Future<T> future = queues.get(current).apply(json);
                            if (Objects.isNull(future)) {
                                /*
                                 * When null found, skip current
                                 */
                                return To.future(json);
                            } else {
                                return future
                                        /*
                                         * Replace the result with successed item here
                                         * If success
                                         * -- replace previous response with next
                                         * If failure
                                         * -- returned current json and replace previous response with current
                                         *
                                         * The step stopped
                                         */
                                        .compose(response::future)
                                        .otherwise(Ux.otherwise(() -> response.add(json).get()));
                            }
                        }).otherwise(Ux.otherwise(() -> response.get()));
                    }
                    return first;
                }
            }
        }
    }

    @SuppressWarnings("all")
    static <T> Future<JsonObject> toJsonFuture(
            final String pojo,
            final CompletableFuture<T> completableFuture
    ) {
        final Promise<JsonObject> future = Promise.promise();
        Fn.safeSemi(null == completableFuture, null,
                () -> future.complete(new JsonObject()),
                () -> completableFuture.thenAcceptAsync((item) -> Fn.safeSemi(
                        null == item, null,
                        () -> future.complete(new JsonObject()),
                        () -> future.complete(To.toJObject(item, pojo))
                )).exceptionally((ex) -> {
                    LOGGER.jvm(ex);
                    future.fail(ex);
                    return null;
                }));
        return future.future();
    }

    @SuppressWarnings("all")
    static <T> Future<JsonArray> toArrayFuture(
            final String pojo,
            final CompletableFuture<List<T>> completableFuture
    ) {
        final Promise<JsonArray> future = Promise.promise();
        Fn.safeSemi(null == completableFuture, null,
                () -> future.complete(new JsonArray()),
                () -> completableFuture.thenAcceptAsync((item) -> Fn.safeSemi(
                        null == item, null,
                        () -> future.complete(new JsonArray()),
                        () -> future.complete(To.toJArray(item, pojo))
                )).exceptionally((ex) -> {
                    LOGGER.jvm(ex);
                    future.fail(ex);
                    return null;
                }));
        return future.future();
    }

    @SuppressWarnings("all")
    static <T> Future<JsonObject> toUpsertFuture(final T entity, final String pojo,
                                                 final Supplier<Future<JsonObject>> supplier,
                                                 final Function<JsonObject, JsonObject> updateFun) {
        return Fn.match(
                Fn.fork(() -> Future.succeededFuture(To.toJObject(entity, pojo))
                        .compose(item -> null == updateFun ?
                                Future.succeededFuture(item) :
                                Future.succeededFuture(updateFun.apply(item))
                        )
                ),
                Fn.branch(null == entity, supplier));
    }

    static <T> Function<Throwable, Future<T>> toErrorFuture(final Supplier<T> input) {
        return ex -> {
            if (Objects.nonNull(ex)) ex.printStackTrace();
            return Future.succeededFuture(input.get());
        };
    }
}
