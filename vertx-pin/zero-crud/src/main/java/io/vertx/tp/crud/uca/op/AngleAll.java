package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.up.uca.jooq.UxJooq;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AngleAll implements Angle {
    @Override
    public Future<JsonArray> runAsync(final JsonObject input, final IxIn in) {
        Ix.Log.filters(this.getClass(), "( All ) Condition: {0}", input);
        if (in.canJoin()) {
            return Ix.fetchFn(in).apply(input);
        } else {
            final UxJooq jooq = IxPin.jooq(in);
            return jooq.fetchJAsync(input);
        }
    }
}
