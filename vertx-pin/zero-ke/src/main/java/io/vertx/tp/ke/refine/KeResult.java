package io.vertx.tp.ke.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KValue;
import io.vertx.up.util.Ut;

class KeResult {

    static JsonObject bool(final String key, final boolean checked) {
        final KValue.Bool response = checked ?
            KValue.Bool.SUCCESS : KValue.Bool.FAILURE;
        return new JsonObject().put(key, response.name());
    }

    static Boolean bool(final JsonObject checkedJson) {
        final String result = checkedJson.getString(KName.RESULT);
        final KValue.Bool resultValue = Ut.toEnum(() -> result, KValue.Bool.class, KValue.Bool.FAILURE);
        return KValue.Bool.SUCCESS == resultValue;
    }

    static JsonObject array(final JsonArray array) {
        return new JsonObject().put(KName.DATA, array);
    }
}
