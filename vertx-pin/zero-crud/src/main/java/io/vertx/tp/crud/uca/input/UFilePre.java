package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.feature.Attachment;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class UFilePre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        return Ix.fileFn(in, (criteria, dataArray) -> Ke.channel(
            Attachment.class,                               // Component
            JsonArray::new,                             // JsonArray Data
            file -> file.saveAsync(criteria, dataArray) // Execution Logical
        )).apply(data);
    }
}
