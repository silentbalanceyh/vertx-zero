package io.vertx.mod.crud.uca.input;

import io.horizon.spi.feature.Attachment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.refine.Ix;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DFilePre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        return Ix.fileFn(in, (criteria, dataArray) -> Ux.channel(
            Attachment.class,                                   // Component
            JsonArray::new,                                     // JsonArray Data
            file -> file.removeAsync(criteria)                  // Execution Logical
        )).apply(data);
    }

    /*
     * Batch Deleted
     */
    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        final List<Future<JsonObject>> futures = new ArrayList<>();
        Ut.itJArray(data).forEach(json -> futures.add(this.inJAsync(json, in)));
        return Fn.combineA(futures);
    }
}
