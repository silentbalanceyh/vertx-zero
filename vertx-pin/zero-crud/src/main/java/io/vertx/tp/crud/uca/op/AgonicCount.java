package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicCount implements Agonic {
    @Override
    public Future<JsonObject> runAsync(final JsonObject input, final IxIn in) {
        Ix.Log.filters(this.getClass(), "( Count ) Condition: {0}", input);
        final Function<Long, Future<JsonObject>> outFn =
                counter -> Ux.future(new JsonObject().put(KName.COUNT, counter));
        if (in.canJoin()) {
            return Ix.countFn(in).apply(input).compose(outFn);
        } else {
            final UxJooq jooq = IxPin.jooq(in);
            return jooq.countAsync(input).compose(outFn);
        }
    }
}
