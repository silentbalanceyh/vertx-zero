package io.vertx.tp.crud.uca.next;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class OkJActive implements OkJ<JsonObject> {
    @Override
    public Future<JsonObject> ok(final JsonObject active, final Object standBy) {
        return Ux.future(active);
    }
}
