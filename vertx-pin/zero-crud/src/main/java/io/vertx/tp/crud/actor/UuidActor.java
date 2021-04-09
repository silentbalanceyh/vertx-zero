package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.metadata.KField;
import io.vertx.tp.ke.atom.metadata.KModule;
import io.vertx.up.util.Ut;

import java.util.UUID;

/*
 * {
 *    "key": "uuid"
 * }
 */
class UuidActor extends AbstractActor {

    @Override
    public JsonObject proc(final JsonObject data, final KModule config) {
        final KField field = config.getField();
        /* Primary Key Add */
        final String keyField = field.getKey();
        if (Ut.notNil(keyField)) {
            /* Value Extract */
            final String keyValue = data.getString(keyField);
            if (Ut.isNil(keyValue)) {
                /* Primary Key */
                data.put(keyField, UUID.randomUUID().toString());
            }
        }
        return data;
    }
}
