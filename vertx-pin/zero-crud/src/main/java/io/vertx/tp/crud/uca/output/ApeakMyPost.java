package io.vertx.tp.crud.uca.output;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ApeakMyPost implements Post<JsonArray> {

    @Override
    public Future<JsonArray> outAsync(final Object active, final Object standBy) {
        return Ux.future((JsonArray) active);
    }
}
