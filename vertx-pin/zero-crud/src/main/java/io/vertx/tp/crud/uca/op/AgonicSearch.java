package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicSearch implements Agonic {
    @Override
    public Future<JsonObject> runAsync(final JsonObject input, final IxIn in) {
        return Ix.searchFn(in).apply(input);
    }
}
