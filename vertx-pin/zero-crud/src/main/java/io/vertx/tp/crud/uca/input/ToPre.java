package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.up.commune.exchange.DictFabric;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ToPre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxIn in) {
        final Future<DictFabric> future = Ix.onFabric(in);
        if (Objects.isNull(future)) {
            return Ux.future(data);
        }
        return future.compose(fabric -> fabric.inTo(data));
    }

    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxIn in) {
        final Future<DictFabric> future = Ix.onFabric(in);
        if (Objects.isNull(future)) {
            return Ux.future(data);
        }
        return future.compose(fabric -> fabric.inTo(data));
    }
}
