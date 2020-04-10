package io.vertx.tp.crud.refine;

import io.vertx.core.json.JsonObject;

class IxIs {

    static boolean isExist(final JsonObject result) {
        final Long counter = result.getLong("count", 0L);
        return 0 < counter;
    }
}
