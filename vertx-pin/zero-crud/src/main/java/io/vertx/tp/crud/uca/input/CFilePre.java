package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.optic.feature.Attachment;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class CFilePre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        return Ix.fileFn(in, (criteria, dataArray) -> Ux.channel(
            Attachment.class,                       // Component
            JsonArray::new,                     // JsonArray Data
            file -> file.uploadAsync(dataArray) // Execution Logical
        )).apply(data);
    }
}
