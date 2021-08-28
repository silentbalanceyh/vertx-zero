package io.vertx.tp.ke.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KResult;
import io.vertx.up.util.Ut;

class KeResult {

    static JsonObject bool(final String key, final boolean checked) {
        final KResult.Bool response = checked ?
            KResult.Bool.SUCCESS : KResult.Bool.FAILURE;
        return new JsonObject().put(key, response.name());
    }

    static Boolean bool(final JsonObject checkedJson) {
        final String result = checkedJson.getString(KName.RESULT);
        final KResult.Bool resultValue = Ut.toEnum(() -> result, KResult.Bool.class, KResult.Bool.FAILURE);
        return KResult.Bool.SUCCESS == resultValue;
    }

    static JsonObject array(final JsonArray array) {
        return new JsonObject().put(KName.DATA, array);
    }
}
