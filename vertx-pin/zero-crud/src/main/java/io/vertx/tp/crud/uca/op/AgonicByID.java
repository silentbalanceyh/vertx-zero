package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxKit;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.crud.uca.tran.Co;
import io.vertx.tp.ke.atom.specification.KModule;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicByID implements Agonic {
    @Override
    @SuppressWarnings("all")
    public Future<JsonObject> runJAsync(final JsonObject input, final IxMod in) {
        final UxJooq jooq = IxPin.jooq(in);
        return jooq.fetchOneAsync(input).compose(entity -> {
            if (Objects.isNull(entity)) {
                // STOP: Return to stop code executing
                return IxKit.success204Pre();
            }
            // For Format Beauty
            final KModule module = in.module();
            final JsonObject active = Ux.toJson(entity, module.getPojo());
            // Try to connecting
            final IxMod connect = in.connecting(active);
            if (Objects.isNull(connect)) {
                // STOP: Return to stop code executing
                return Ux.future(active);
            }
            // Next Json
            final Co<JsonObject, JsonObject, JsonObject, JsonObject> co = Co.nextQ(in, false);
            return co.next(input, active)
                .compose(params -> this.runJAsync(params, connect))
                .compose(standBy -> co.ok(active, standBy));
        });
    }
}
