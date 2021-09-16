package io.vertx.tp.crud.uca.tran;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NtAData implements NtA<JsonArray> {

    private transient final IxMod in;

    NtAData(final IxMod in) {
        this.in = in;
    }

    @Override
    public Future<JsonArray> next(final JsonArray input, final JsonArray active) {
        if (this.in.canJoin()) {
            final JsonArray zip = Ut.elementZip(active, input);
            final JsonArray normalized = new JsonArray();
            Ut.itJArray(zip).forEach(json -> {
                final JsonObject dataSt = this.in.dataIn(json);
                normalized.add(json.copy().mergeIn(dataSt, true));
            });
            return Ux.future(normalized);
        } else {
            // There is no joined module on current
            return Ux.future(active);
        }
    }

    @Override
    public Future<JsonArray> ok(final JsonArray active, final JsonArray standBy) {
        if (this.in.canJoin()) {
            final JsonArray zip = Ut.elementZip(active, standBy, KName.KEY);
            final JsonArray normalized = new JsonArray();
            Ut.itJArray(zip).forEach(json -> {
                final JsonObject dataSt = this.in.dataOut(json);
                normalized.add(json.copy().mergeIn(dataSt, true));
            });
            return Ux.future(normalized);
        } else {
            // There is no joined module on current
            return Ux.future(active.copy());
        }
    }
}
