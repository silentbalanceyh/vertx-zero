package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.atom.KModule;
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
    public Future<JsonObject> inAsync(final JsonObject data, final IxIn in) {
        /* Header */
        final Envelop envelop = in.envelop();
        final KModule module = in.module();
        final MultiMap headers = envelop.headers();
        /* Header Attached */
        final JsonObject config = module.getHeader();
        if (Objects.nonNull(config)) {
            Ut.<String>itJObject(config, (to, from) -> {
                final String value = headers.get(to);
                if (Ut.notNil(value)) {
                    data.put(from, value);
                }
            });
        }
        return Future.succeededFuture(data);
    }
}
