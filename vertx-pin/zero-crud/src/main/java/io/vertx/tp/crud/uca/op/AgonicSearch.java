package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.up.uca.jooq.UxJooq;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicSearch implements Agonic {
    @Override
    public Future<JsonObject> runJAsync(final JsonObject input, final IxIn in) {
        Ix.Log.filters(this.getClass(), "( Search ) Condition: {0}", input);
        if (in.canJoin()) {
            return Ix.searchFn(in).apply(input);
        } else {
            final UxJooq jooq = IxPin.jooq(in);
            return jooq.searchAsync(input);
        }
    }
}
