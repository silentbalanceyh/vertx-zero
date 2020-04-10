package io.vertx.up.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

class Define {

    static JsonArray sureJArray(final JsonArray array) {
        if (Ut.isNil(array)) {
            return new JsonArray();
        } else {
            return array;
        }
    }

    static JsonObject sureJObject(final JsonObject object) {
        if (Ut.isNil(object)) {
            return new JsonObject();
        } else {
            return object;
        }
    }
}
