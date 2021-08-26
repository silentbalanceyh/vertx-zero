package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class QPkPre implements Pre {
    @Override
    public Future<JsonObject> inAJAsync(final JsonArray keys, final IxIn in) {
        if (Ut.isNil(keys)) {
            return Ux.futureJ();
        }
        // Module Processing
        final String keyField = in.module().getField().getKey();
        /* Filters */
        final JsonObject condition = new JsonObject();
        final JsonArray keyArray = new JsonArray();
        /* Two */
        keys.stream().forEach(item -> {
            if (item instanceof String) {
                keyArray.add(item);
            } else if (item instanceof JsonObject) {
                /* Key Value */
                final String value = ((JsonObject) item).getString(keyField);
                if (Ut.notNil(value)) {
                    keyArray.add(value);
                }
            }
        });
        condition.put(keyField + ",i", keyArray);
        return Ux.future(condition);
    }
}
