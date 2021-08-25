package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AgonicSearch implements Agonic {
    @Override
    public Future<Envelop> runAsync(final JsonObject input, final IxIn in) {
        return Ix.search(in).apply(input)
                .compose(item -> Ux.future(Envelop.success(item)));
    }

    @Override
    public Future<Envelop> runBAsync(final JsonArray input, final IxIn in) {
        return null;
    }
}
