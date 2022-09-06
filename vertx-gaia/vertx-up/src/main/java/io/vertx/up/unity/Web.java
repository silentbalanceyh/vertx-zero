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
import io.vertx.up.util.Ut;

import java.util.Objects;
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
