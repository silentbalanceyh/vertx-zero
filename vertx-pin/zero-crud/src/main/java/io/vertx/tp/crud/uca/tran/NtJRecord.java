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
             * 1. Joined Key Processing
             * 2. Mapping configuration
             */
            final JsonObject dataSt = this.in.dataPoint(input, active);
            // Remove `key` of current
            final String key = this.in.module().getField().getKey();
            dataSt.remove(key);
            return Ux.future(dataSt);
        } else {
            // There is no joined module on current
            return Ux.future(active.copy());
        }
    }
}
