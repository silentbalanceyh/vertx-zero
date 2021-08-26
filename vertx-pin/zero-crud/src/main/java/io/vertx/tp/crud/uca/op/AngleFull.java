package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.Apeak;
import io.vertx.up.uca.jooq.UxJooq;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AngleFull implements Agonic {
    @Override
    public Future<JsonArray> runJAAsync(final JsonObject input, final IxIn in) {
        /* Get Stub */
        final UxJooq jooq = IxPin.jooq(in);
        return Ke.channel(Apeak.class, JsonArray::new, stub -> stub.on(jooq).fetchFull(input));
    }
}
