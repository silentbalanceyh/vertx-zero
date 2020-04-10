package io.vertx.tp.ke.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.util.Ut;

class KeResult {

    static JsonObject bool(final String key, final boolean checked) {
        final io.vertx.tp.ke.cv.KeResult.Bool response = checked ?
                io.vertx.tp.ke.cv.KeResult.Bool.SUCCESS : io.vertx.tp.ke.cv.KeResult.Bool.FAILURE;
        return new JsonObject().put(key, response.name());
    }

    static Boolean bool(final JsonObject checkedJson) {
        final String result = checkedJson.getString(KeField.RESULT);
        final io.vertx.tp.ke.cv.KeResult.Bool resultValue = Ut.toEnum(() -> result, io.vertx.tp.ke.cv.KeResult.Bool.class, io.vertx.tp.ke.cv.KeResult.Bool.FAILURE);
        return io.vertx.tp.ke.cv.KeResult.Bool.SUCCESS == resultValue;
    }

    static JsonObject array(final JsonArray array) {
        return new JsonObject().put(KeField.DATA, array);
    }
}
