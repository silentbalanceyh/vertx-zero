package io.vertx.tp.crud.uca.tran;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class NtAQr implements Co<JsonObject, JsonArray, JsonArray, JsonArray> {
    private transient final Co record;

    NtAQr(final IxMod in) {
        this.record = Fn.poolThread(Pooled.CO_MAP, () -> new NtJRecord(in), NtJRecord.class.getName() + in.module().getIdentifier());
    }

    @Override
    public Future<JsonArray> next(final JsonObject input, final JsonArray active) {
        return null;
    }
}
