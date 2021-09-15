package io.vertx.tp.crud.uca.tran;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class OkAActive implements OkA<JsonObject> {
    @Override
    public Future<JsonArray> ok(final JsonArray active, final JsonArray standBy) {
        return Ux.future(active);
    }
}
