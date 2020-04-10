package io.vertx.up.unity;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Envelop;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
                message.reply(Envelop.failure(To.toError(Web.class, handler.cause())));
            }
        };
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
        return Combine.thenCombine(futures.toArray(new Future[]{}))
                .compose(response -> {
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

    static <T> Function<JsonObject, Future<JsonObject>> toAttachJson(
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
}
