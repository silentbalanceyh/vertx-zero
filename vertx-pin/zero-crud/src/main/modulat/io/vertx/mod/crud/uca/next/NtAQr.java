package io.vertx.mod.crud.uca.next;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.cv.Pooled;
import io.vertx.mod.crud.uca.desk.IxMod;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class NtAQr implements Co<JsonObject, JsonArray, JsonArray, JsonArray> {
    private transient final IxMod in;
    private transient final Co<JsonArray, JsonArray, JsonArray, JsonArray> record;

    NtAQr(final IxMod in) {
        this.in = in;
        this.record = Pooled.CC_CO.pick(() -> new NtAData(in), NtAData.class.getName() + in.keyPool());
        // Fn.po?lThread(Pooled.CO_MAP, () -> new NtAData(in), NtAData.class.getName() + in.keyPool());
    }

    @Override
    public Future<JsonArray> next(final JsonObject input, final JsonArray active) {
        return this.record.next(new JsonArray(), active);
    }

    @Override
    public Future<JsonArray> ok(JsonArray active, JsonArray standBy) {
        return this.record.next(active, standBy);
    }
}
