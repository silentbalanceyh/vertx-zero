package io.vertx.mod.crud.uca.op;

import io.horizon.spi.ui.ApeakMy;
import io.horizon.spi.web.Seeker;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.cv.em.QrType;
import io.vertx.mod.crud.init.IxPin;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.mod.crud.uca.input.Pre;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicView implements Agonic {

    @Override
    public Future<JsonObject> runJAsync(final JsonObject input, final IxMod in) {
        final UxJooq jooq = IxPin.jooq(in);
        return Ux.channel(Seeker.class, JsonObject::new, seeker -> seeker.on(jooq).fetchImpact(input))
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
            .compose(params -> Pre.qr(QrType.BY_VK).inJAsync(params, in))
            .compose(params -> Ux.channel(ApeakMy.class, JsonObject::new,
                stub -> stub.on(jooq).saveMy(params, params.getJsonObject(KName.DATA))));
    }
}
