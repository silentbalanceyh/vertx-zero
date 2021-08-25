package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.atom.KField;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.UUID;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class UuidPre implements Pre {
    @Override
    public Future<JsonObject> inAsync(final JsonObject data, final IxIn in) {
        final KModule module = in.module();
        final KField field = module.getField();
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
        return Ux.future(data);
    }
}
