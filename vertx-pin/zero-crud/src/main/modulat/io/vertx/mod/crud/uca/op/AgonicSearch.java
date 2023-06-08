package io.vertx.mod.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.init.IxPin;
import io.vertx.mod.crud.refine.Ix;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import static io.vertx.mod.crud.refine.Ix.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicSearch implements Agonic {
    @Override
    public Future<JsonObject> runJAsync(final JsonObject input, final IxMod in) {
        LOG.Filter.info(this.getClass(), "( Search ) Condition: {0}", input);
        if (in.canJoin()) {
            return Ix.searchFn(in).apply(input).compose(Fn.ofPage(KName.METADATA))
                // Response Format
                .compose(response -> Ux.future(Ix.serializeP(response, in.module(), in.connect())));
        } else {
            final UxJooq jooq = IxPin.jooq(in);
            return jooq.searchAsync(input).compose(Fn.ofPage(KName.METADATA))
                // Response Format
                .compose(response -> Ux.future(Ix.serializeP(response, in.module(), in.connect())));
        }
    }
}
