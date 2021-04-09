package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.metadata.KField;
import io.vertx.tp.ke.atom.metadata.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/*
 * key for primary key generation here
 * {
 *      "key": ""
 * }
 */
class KeyActor extends AbstractActor {

    @Override
    public JsonObject proc(final JsonObject data, final KModule config) {
        final Envelop request = this.getRequest();
        final KField field = config.getField();
        /* Primary Key Add */
        if (Ut.notNil(field.getKey())) {
            final String originalKey = data.getString(field.getKey());
            /*
             * null for set key
             */
            if (Ut.isNil(originalKey)) {
                final String keyValue = Ux.getString1(request);
                if (Ut.notNil(keyValue)) {
                    data.put(field.getKey(), keyValue);
                }
            }
            /*
             * If the key existing, do not set `key = uuid` format to input data to
             * avoid key overwrite
             * Fix bug: Could not update linker data here.
             */
        }
        return data;
    }
}
