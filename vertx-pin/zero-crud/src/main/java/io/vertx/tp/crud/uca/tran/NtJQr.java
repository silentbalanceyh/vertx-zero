package io.vertx.tp.crud.uca.tran;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.up.fn.Fn;
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
        this.record = Fn.poolThread(Pooled.CO_MAP, () -> new NtJRecord(in), NtJRecord.class.getName() + in.module().getIdentifier());
    }

    @Override
    public Future<JsonObject> next(final JsonObject input, final JsonObject active) {
        final JsonObject params = this.in.dataIn(active);
        return Ux.future(params);
    }

    @Override
    public Future<JsonObject> ok(JsonObject active, JsonObject standBy) {
        return this.record.ok(active, standBy);
    }
}
