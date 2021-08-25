package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AgonicExisting implements Agonic {
    @Override
    public Future<JsonObject> runAsync(final JsonObject input, final IxIn in) {
        return Ix.countFn(in).apply(input)
                .compose(counter -> Ux.future(new JsonObject().put(KName.COUNT, counter)));
    }
}
