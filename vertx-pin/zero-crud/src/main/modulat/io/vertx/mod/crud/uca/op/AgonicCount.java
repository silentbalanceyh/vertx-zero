package io.vertx.mod.crud.uca.op;

import io.horizon.eon.VString;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.init.IxPin;
import io.vertx.mod.crud.refine.Ix;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.function.Function;

import static io.vertx.mod.crud.refine.Ix.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicCount implements Agonic {
    @Override
    public Future<JsonObject> runJAsync(final JsonObject input, final IxMod in) {
        if (!input.containsKey(VString.EMPTY)) {
            input.put(VString.EMPTY, Boolean.TRUE);
        }
        LOG.Filter.info(this.getClass(), "( Count ) Condition: {0}", input);
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
