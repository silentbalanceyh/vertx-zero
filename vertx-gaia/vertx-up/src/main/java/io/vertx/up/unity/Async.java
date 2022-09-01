package io.vertx.up.unity;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KValue;
import io.vertx.up.eon.Values;
import io.vertx.up.experiment.channel.Pocket;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.function.Supplier;

class Async {

    private static final Annal LOGGER = Annal.get(Async.class);

    static <T> Future<T> fromAsync(final CompletionStage<T> state) {
        final Promise<T> promise = Promise.promise();
        state.whenComplete((result, error) -> {
            if (Objects.isNull(error)) {
                promise.complete(result);
            } else {
                promise.fail(error);
            }
        });
        return promise.future();
    }

    static <T> Future<T> future(final T input, final Set<Function<T, Future<T>>> set) {
        final List<Future<T>> futures = new ArrayList<>();
        set.stream().map(consumer -> consumer.apply(input)).forEach(futures::add);
        Fn.combineT(futures).compose(nil -> {
            LOGGER.info("「Job Plugin」 There are `{0}` jobs that are finished successfully!", String.valueOf(set.size()));
            return To.future(nil);
        });
        return To.future(input);
    }

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
                                    .otherwise(Debug.otherwise(() -> response.add(json).get()));
                            }
                        }).otherwise(Debug.otherwise(() -> response.get()));
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
        // Default Case
        if (Objects.isNull(entity)) {
            return supplier.get();
        }
        final JsonObject params = To.toJObject(entity, pojo);

        // Update Function == null
        if (Objects.isNull(updateFun)) {
            return Future.succeededFuture(params);
        }

        // Update Executor
        return Future.succeededFuture(updateFun.apply(params));
    }

    static <T> Function<Throwable, Future<T>> toErrorFuture(final Supplier<T> input) {
        return ex -> {
            if (Objects.nonNull(ex)) {
                ex.printStackTrace();
            }
            return Future.succeededFuture(input.get());
        };
    }

    static JsonObject bool(final String key, final boolean checked) {
        final KValue.Bool response = checked ?
            KValue.Bool.SUCCESS : KValue.Bool.FAILURE;
        return new JsonObject().put(key, response.name());
    }

    static Boolean bool(final JsonObject checkedJson) {
        final String result = checkedJson.getString(KName.RESULT);
        final KValue.Bool resultValue = Ut.toEnum(() -> result, KValue.Bool.class, KValue.Bool.FAILURE);
        return KValue.Bool.SUCCESS == resultValue;
    }

    static JsonObject array(final JsonArray array) {
        return new JsonObject().put(KName.DATA, array);
    }


    static <T, O> Future<O> channel(final Class<T> clazz, final Supplier<O> supplier,
                                    final Function<T, Future<O>> executor) {
        final T channel = Pocket.lookup(clazz);
        if (Objects.isNull(channel)) {
            LOGGER.warn("「SL Channel」Channel {0} null", clazz.getName());
            return To.future(supplier.get());
        } else {
            LOGGER.debug("「SL Channel」Channel Async selected {0}, {1}",
                channel.getClass().getName(), String.valueOf(channel.hashCode()));
            return executor.apply(channel);
        }
    }


    static <T, O> O channelSync(final Class<T> clazz, final Supplier<O> supplier,
                                final Function<T, O> executor) {
        final T channel = Pocket.lookup(clazz);
        if (Objects.isNull(channel)) {
            LOGGER.warn("「SL Channel」Channel Sync {0} null", clazz.getName());
            return supplier.get();
        } else {
            LOGGER.debug("「SL Channel」Channel Sync selected {0}, {1}",
                channel.getClass().getName(), String.valueOf(channel.hashCode()));
            return executor.apply(channel);
        }
    }

    static <T, O> Future<O> channelAsync(final Class<T> clazz, final Supplier<Future<O>> supplier,
                                         final Function<T, Future<O>> executor) {
        final T channel = Pocket.lookup(clazz);
        if (Objects.isNull(channel)) {
            LOGGER.warn("「SL Channel」Channel Async {0} null", clazz.getName());
            return supplier.get();
        } else {
            LOGGER.debug("「SL Channel」Channel Async selected {0}, {1}",
                channel.getClass().getName(), String.valueOf(channel.hashCode()));
            return executor.apply(channel);
        }
    }
}
