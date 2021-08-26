package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.crud.uca.desk.IxIn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AgonicExport implements Agonic {
    @Override
    public Future<JsonArray> runAAsync(final JsonArray input, final IxIn in) {
        return Agonic.super.runAAsync(input, in);
    }
}
