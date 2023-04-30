package io.vertx.up.fn;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author lang : 2023/4/30
 */
class If {
    @SuppressWarnings("unchecked")
    static <I, T> Future<T> ifDefault(final I input, final Future<T> future) {
        if (Objects.isNull(input)) {
            return Future.succeededFuture();
        }
        if (input instanceof final JsonArray array) {
            // JsonArray Null Checking
            if (array.isEmpty()) {
                final T emptyA = (T) new JsonArray();
                return Future.succeededFuture(emptyA);
            }
        } else if (input instanceof final JsonObject object) {
            // JsonObject Null Checking
            if (Ut.isNil(object)) {
                final T emptyJ = (T) new JsonObject();
                return Future.succeededFuture(emptyJ);
            }
        }
        return future;
    }
}
