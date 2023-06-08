package io.vertx.mod.crud.uca.next;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.cv.Pooled;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class NtJQr implements Co<JsonObject, JsonObject, JsonObject, JsonObject> {
    private transient final IxMod in;
    private transient final Co<JsonObject, JsonObject, JsonObject, JsonObject> record;

    NtJQr(final IxMod in) {
        this.in = in;
        this.record = Pooled.CC_CO.pick(() -> new NtJData(in), NtJData.class.getName() + in.keyPool());
        // Fn.po?lThread(Pooled.CO_MAP, () -> new NtJData(in), NtJData.class.getName() + in.keyPool());
    }

    @Override
    public Future<JsonObject> next(final JsonObject input, final JsonObject active) {
        if (this.in.canJoin()) {
            final JsonObject params = this.in.dataCond(active);
            return Ux.future(params);
        } else {
            return Ux.future(active.copy());
        }
    }

    @Override
    public Future<JsonObject> ok(JsonObject active, JsonObject standBy) {
        if (this.in.canJoin()) {
            return this.record.ok(active, standBy);
        } else {
            return Ux.future(active.copy());
        }
    }
}
