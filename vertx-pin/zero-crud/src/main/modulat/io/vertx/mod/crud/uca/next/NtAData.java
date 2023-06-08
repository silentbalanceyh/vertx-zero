package io.vertx.mod.crud.uca.next;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import static io.vertx.mod.crud.refine.Ix.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NtAData implements Co<JsonArray, JsonArray, JsonArray, JsonArray> {

    private transient final IxMod in;

    NtAData(final IxMod in) {
        this.in = in;
    }

    @Override
    public Future<JsonArray> next(final JsonArray input, final JsonArray active) {
        if (this.in.canJoin()) {
            final JsonArray dataSt = this.in.dataIn(input, active);
            // Remove `key` of current
            final String key = this.in.module().getField().getKey();
            Ut.itJArray(dataSt).forEach(json -> json.remove(key));
            LOG.Web.info(this.getClass(), "Data In: {0}", dataSt.encode());
            return Ux.future(dataSt);
        } else {
            // There is no joined module on current
            return Ux.future(active);
        }
    }

    @Override
    public Future<JsonArray> ok(final JsonArray active, final JsonArray standBy) {
        if (this.in.canJoin()) {
            return Ux.future(this.in.dataOut(active, standBy));
        } else {
            // There is no joined module on current
            return Ux.future(active.copy());
        }
    }
}
