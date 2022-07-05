package io.vertx.up.unity;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;

class Web {

    static <T> Handler<AsyncResult<T>> toHandler(
        final Message<Envelop> message
    ) {
        return handler -> {
            if (handler.succeeded()) {
                message.reply(To.toEnvelop(handler.result()));
            } else {
                // Readible codec for configured information, error flow needed.
                if (null != handler.cause()) {
                    handler.cause().printStackTrace();
                }
                message.reply(Envelop.failure(Ut.toError(Web.class, handler.cause())));
            }
        };
    }

    static <T> Future<T> toFuture(final Consumer<Handler<AsyncResult<T>>> handler) {
        final Promise<T> promise = Promise.promise();
        handler.accept(result -> {
            if (result.succeeded()) {
                promise.complete(result.result());
            } else {
                promise.fail(result.cause());
            }
        });
        return promise.future();
    }

    @SuppressWarnings("all")
    static <T> Future<JsonObject> toAttach(
        final JsonObject input,
        final Function<T, Future<JsonObject>> function) {
        /*
         * Normalized key = index
         */
        final ConcurrentMap<String, String> indexMap = new ConcurrentHashMap<>();
        final List<Future<JsonObject>> futures = new ArrayList<>();
        final Iterator<String> it = input.fieldNames().iterator();
        int index = 0;
        while (it.hasNext()) {
            /*
             * Field extract
             */
            final String field = it.next();
            final T value = (T) input.getValue(field);
            if (Objects.nonNull(value)) {
                indexMap.put(field, String.valueOf(index));
                futures.add(function.apply(value));
                index++;
            }
        }
        return Fn.combineJ(futures.toArray(new Future[]{})).compose(response -> {
            final JsonObject finalJson = new JsonObject();
            final JsonObject reference = (JsonObject) response;
            indexMap.forEach((field, indexKey) -> {
                final JsonObject data = reference.getJsonObject(indexKey);
                if (Ut.notNil(data)) {
                    finalJson.put(field, data.copy());
                }
            });
            return To.future(finalJson);
        });
    }

    static <T> Function<JsonObject, Future<JsonObject>> toAttachJ(
        final String field,
        final Function<T, Future<JsonObject>> function
    ) {
        return json -> {
            if (Ut.isNil(json) || !json.containsKey(field)) {
                return To.future(json);
            } else {
                final JsonObject combine = json.getJsonObject(field);
                if (Ut.isNil(combine)) {
                    return To.future(json);
                } else {
                    return toAttach(combine, function).compose(response -> {
                        if (Ut.notNil(response)) {
                            json.put(field, response);
                        }
                        return To.future(json);
                    });
                }
            }
        };
    }

    static <T> Function<T, Future<JsonObject>> toAttachJ(final String field, final JsonObject data) {
        return json -> {
            if (Objects.nonNull(json)) {
                data.put(field, json);
            }
            return To.future(data);
        };
    }

    @SuppressWarnings("unchecked")
    static <T> Function<JsonObject, Future<JsonObject>> toAttach(
        final String field,
        final Function<T, Future<JsonObject>> function) {
        return json -> {
            if (Ut.isNil(json) || !json.containsKey(field)) {
                return To.future(json);
            } else {
                final T value = (T) json.getValue(field);
                if (Objects.isNull(value)) {
                    return To.future(json);
                } else {
                    return function.apply(value).compose(data -> {
                        if (Ut.notNil(data)) {
                            json.put(field, data);
                        }
                        return To.future(json);
                    });
                }
            }
        };
    }

    static JsonObject pageData(JsonArray data, Long count) {
        if (Ut.isNil(data)) {
            data = new JsonArray();
        }
        if (Objects.isNull(count) || 0 > count) {
            count = 0L;
        }
        return new JsonObject().put(KName.LIST, data).put(KName.COUNT, count);
    }

    static JsonObject pageData(final JsonObject pageData, final Function<JsonArray, JsonArray> function) {
        final JsonArray data = Ut.valueJArray(pageData.getJsonArray(KName.LIST));
        final JsonArray updated;
        if (Objects.nonNull(function)) {
            updated = function.apply(data);
        } else {
            updated = data;
        }
        return pageData.put(KName.LIST, updated);
    }
}
