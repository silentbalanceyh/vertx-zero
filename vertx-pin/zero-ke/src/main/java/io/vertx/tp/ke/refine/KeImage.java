package io.vertx.tp.ke.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import io.vertx.up.fn.Fn;

import java.util.function.Function;

class KeImage {
    static Function<JsonObject, Future<JsonObject>> image(final String field) {
        return response -> Fn.getJvm(Future.succeededFuture(new JsonObject()), () -> {
            final String image = response.getString(field);
            if (!Ut.isNil(image)) {
                response.put(field, new JsonArray(image));
            }
            return Ux.future(response);
        }, response);
    }
}
