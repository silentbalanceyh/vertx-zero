package io.vertx.mod.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.cv.em.QrType;
import io.vertx.mod.crud.init.IxPin;
import io.vertx.mod.crud.refine.Ix;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.mod.crud.uca.input.Pre;
import io.vertx.up.uca.jooq.UxJooq;

import static io.vertx.mod.crud.refine.Ix.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicFetch implements Agonic {
    @Override
    public Future<JsonArray> runJAAsync(final JsonObject input, final IxMod in) {
        LOG.Filter.info(this.getClass(), "( All ) Condition: {0}", input);
        if (in.canJoin()) {
            return Ix.fetchFn(in).apply(input);
        } else {
            final UxJooq jooq = IxPin.jooq(in);
            return jooq.fetchJAsync(input);
        }
    }

    @Override
    public Future<JsonArray> runAAsync(final JsonArray input, final IxMod in) {
        return Pre.qr(QrType.BY_PK).inAJAsync(input, in)
            .compose(condition -> this.runJAAsync(condition, in));
    }
}
