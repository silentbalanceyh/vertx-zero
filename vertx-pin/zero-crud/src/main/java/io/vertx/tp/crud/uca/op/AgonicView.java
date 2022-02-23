package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.ui.ApeakMy;
import io.vertx.tp.optic.web.Seeker;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicView implements Agonic {

    @Override
    public Future<JsonObject> runJAsync(final JsonObject input, final IxMod in) {
        final UxJooq jooq = IxPin.jooq(in);
        return Ke.channel(Seeker.class, JsonObject::new, seeker -> seeker.on(jooq).fetchImpact(input))
            /* view has value, ignored, */
            /*
             * url processing
             * {
             *      "requestUri": "xxx",
             *      "method": "method",
             * }
             * */
            .compose(params -> Pre.uri().inJAsync(params, in))
            /*
             * {
             *      "user": "xxx",
             *      "habitus": "xxx"
             * }
             */
            .compose(params -> Pre.user().inJAsync(params, in))
            /*
             * data_key calculation
             */
            .compose(params -> Pre.qVk().inJAsync(params, in))
            .compose(params -> Ke.channel(ApeakMy.class, JsonObject::new,
                stub -> stub.on(jooq).saveMy(params, params.getJsonObject(KName.DATA))));
    }
}
