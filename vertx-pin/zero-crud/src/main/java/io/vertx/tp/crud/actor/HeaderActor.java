package io.vertx.tp.crud.actor;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.metadata.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.util.Ut;

/*
 * {
 *      "sigma": "From X-Sigma",
 *      "appId": "From X-App-Id",
 *      "appKey": "From X-App-Key"
 * }
 */
class HeaderActor extends AbstractActor {

    @Override
    public JsonObject proc(final JsonObject data, final KModule config) {
        final Envelop request = this.getRequest();
        final MultiMap headers = request.headers();
        /* Header Data */
        final JsonObject headerConfig = config.getHeader();
        if (null != headerConfig) {
            Ut.itJObject(headerConfig, (to, from) -> {
                final String value = headers.get(to.toString());
                if (Ut.notNil(value)) {
                    data.put(from, value);
                }
            });
        }
        return data;
    }
}
