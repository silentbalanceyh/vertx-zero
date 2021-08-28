package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.ApeakMy;
import io.vertx.tp.optic.Seeker;
import io.vertx.up.uca.jooq.UxJooq;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicMy implements Agonic {
    /*
     * {
     *     "view": "The view name, if not put DEFAULT",
     *     "uri": "http path",
     *     "method": "http method",
     *     "sigma": "The application uniform",
     *     "resourceId": "Seeker result"
     * }
     */
    @Override
    public Future<JsonArray> runJAAsync(final JsonObject input, final IxIn in) {
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
             * Critical workflow
             */
            .compose(params -> Ke.channel(ApeakMy.class, JsonArray::new,
                stub -> stub.on(jooq).fetchMy(params)));
    }
}
