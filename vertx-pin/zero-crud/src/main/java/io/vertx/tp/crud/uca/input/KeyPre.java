package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.atom.specification.KField;
import io.vertx.tp.ke.atom.specification.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class KeyPre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        final Envelop request = in.envelop();
        final KModule module = in.module();
        final KField field = module.getField();
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
        return Ux.future(data);
    }
}
