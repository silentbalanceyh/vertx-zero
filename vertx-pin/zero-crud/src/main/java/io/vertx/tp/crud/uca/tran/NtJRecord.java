package io.vertx.tp.crud.uca.tran;

import io.vertx.core.Future;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxKit;
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
            final JsonObject dataSt = this.in.dataIn(input, active);
            // Remove `key` of current
            final String key = this.in.module().getField().getKey();
            dataSt.remove(key);
            Ix.Log.web(this.getClass(), "Data In: {0}", dataSt.encode());
            return Ux.future(dataSt);
        } else {
            // There is no joined module on current
            return Ux.future(active.copy());
        }
    }

    @Override
    public Future<JsonObject> ok(final JsonObject active, final JsonObject standBy) {
        final HttpStatusCode status = IxKit.getStatus(standBy);
        if (HttpStatusCode.NO_CONTENT == status) {
            /*
             * Major table contain value but the sub-table has no record
             */
            return Ux.future(active);
        } else {
            if (this.in.canJoin()) {
                /*
                 * Result directly
                 */
                final JsonObject dataSt = this.in.dataOut(active, standBy);
                Ix.Log.web(this.getClass(), "Data Out: {0}", dataSt.encode());
                return Ux.future(dataSt);
            } else {
                // There is no joined module on current
                return Ux.future(active.copy());
            }
        }
    }
}
