package io.vertx.tp.crud.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.metadata.KField;
import io.vertx.tp.ke.atom.metadata.KModule;
import io.vertx.up.util.Ut;

import java.util.Objects;

class IxQuery {

    static JsonObject inKeys(final JsonArray array, final KModule config) {
        final KField field = config.getField();
        final String keyField = field.getKey();
        /* Filters */
        final JsonObject filters = new JsonObject();
        final JsonArray keys = new JsonArray();
        array.stream().filter(Objects::nonNull).forEach((item) -> {
            if (JsonObject.class == item.getClass()) {
                /* keyValue */
                final String keyValue = ((JsonObject) item).getString(keyField);
                if (Ut.notNil(keyValue)) {
                    keys.add(keyValue);
                }
            } else {
                keys.add(item);
            }
        });
        /* Filters */
        return filters.put(keyField + ",i", keys);
    }
}
