package io.vertx.mod.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class RPkPre implements Pre {
    @Override
    public Future<JsonObject> inAJAsync(final JsonArray keys, final IxMod in) {
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
                if (Ut.isNotNil(value)) {
                    keyArray.add(value);
                }
            }
        });
        condition.put(keyField + ",i", keyArray);
        return Ux.future(condition);
    }

    @Override
    public Future<JsonObject> inJAsync(final JsonObject input, final IxMod in) {
        if (Ut.isNil(input)) {
            return Ux.futureJ();
        }
        // Module Processing
        final String keyField = in.module().getField().getKey();
        /* Key Value */
        final String value = input.getString(keyField);
        final JsonObject condition = new JsonObject();
        condition.put(keyField, value);
        return Ux.future(condition);
    }
}
