package io.vertx.mod.crud.uca.input;

import io.aeon.experiment.specification.KModule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.atom.shape.KField;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.UUID;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class UuidPre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        final KModule module = in.module();
        this.generateKey(data, module);
        return Ux.future(data);
    }

    private void generateKey(final JsonObject data, final KModule module) {
        final KField field = module.getField();
        /* Primary Key Add */
        final String keyField = field.getKey();
        if (Ut.isNotNil(keyField)) {
            /* Value Extract */
            final String keyValue = data.getString(keyField);
            if (Ut.isNil(keyValue)) {
                /* Primary Key */
                data.put(keyField, UUID.randomUUID().toString());
            }
        }
    }

    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        final KModule module = in.module();
        Ut.itJArray(data).forEach(json -> this.generateKey(json, module));
        return Ux.future(data);
    }
}
