package io.vertx.mod.crud.uca.input;

import io.aeon.experiment.specification.KModule;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.commune.Envelop;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class HeadPre implements Pre {
    /*
     * from, to
     *
     * {
     *      "sigma": "X-Sigma",
     *      "appId": "X-App-Id",
     *      "appKey": "X-App-Key"
     * }
     *
     * Request[to] -> Data[from]
     */
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        /* Header */
        final Envelop envelop = in.envelop();
        final KModule module = in.module();
        final MultiMap headers = envelop.headers();
        /* Header Attached */
        final JsonObject normalized = this.inJAsync(data, headers, module);
        return Future.succeededFuture(normalized);
    }

    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        /* Header */
        final Envelop envelop = in.envelop();
        final KModule module = in.module();
        final MultiMap headers = envelop.headers();
        /* Header Attached */
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(data).map(json -> this.inJAsync(json, headers, module)).forEach(normalized::add);
        return Future.succeededFuture(normalized);
    }

    private JsonObject inJAsync(final JsonObject json, final MultiMap headers,
                                final KModule module) {
        /* Header Attached */
        final JsonObject config = module.getHeader();
        if (Objects.nonNull(config)) {
            Ut.<String>itJObject(config, (to, from) -> {
                final String value = headers.get(to);
                if (Ut.isNotNil(value)) {
                    json.put(from, value);
                }
            });
        }
        return json;
    }
}
