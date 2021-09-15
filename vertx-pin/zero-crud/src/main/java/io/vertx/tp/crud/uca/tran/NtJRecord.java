package io.vertx.tp.crud.uca.tran;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class NtJRecord implements NtJ<JsonObject> {

    private transient final IxMod in;

    NtJRecord(final IxMod in) {
        this.in = in;
    }

    @Override
    public Future<JsonObject> next(final JsonObject input, final JsonObject active) {
        if (this.in.canJoin()) {
            /*
             * 1. Mapping Processing
             */
            return null;
        } else {
            // There is no joined module on current
            return Ux.future(active.copy());
        }
    }
}
