package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExFile;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class FDeletePre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        return Ix.fileFn(in, (criteria, dataArray) -> Ke.channel(
            ExFile.class,                                       // Component
            JsonArray::new,                                     // JsonArray Data
            file -> file.saveAsync(criteria, new JsonArray())   // Execution Logical
        )).apply(data);
    }

    /*
     * Batch Deleted
     */
    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        final List<Future<JsonObject>> futures = new ArrayList<>();
        Ut.itJArray(data).forEach(json -> futures.add(this.inJAsync(json, in)));
        return Ux.thenCombine(futures);
    }
}
