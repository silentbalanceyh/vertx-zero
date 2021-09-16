package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.up.commune.exchange.DiFabric;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DiFromPre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        final Future<DiFabric> future = Ix.onFabric(in);
        return future.compose(Ut.ifNil(() -> data, fabric -> fabric.inFrom(data)));
    }

    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        final Future<DiFabric> future = Ix.onFabric(in);
        return future.compose(Ut.ifNil(() -> data, fabric -> fabric.inFrom(data)));
    }
}
