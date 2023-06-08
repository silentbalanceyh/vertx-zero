package io.vertx.mod.crud.uca.next;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class OkAStandBy implements Co<JsonObject, JsonArray, Object, JsonArray> {
    @Override
    public Future<JsonArray> ok(final JsonArray active, final Object standBy) {
        final JsonArray response;
        if (standBy instanceof JsonArray) {
            response = (JsonArray) standBy;
        } else {
            response = new JsonArray();
        }
        return Ux.future(response);
    }
}
