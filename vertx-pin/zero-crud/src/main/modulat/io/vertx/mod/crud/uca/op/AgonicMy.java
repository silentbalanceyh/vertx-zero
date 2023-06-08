package io.vertx.mod.crud.uca.op;

import io.horizon.spi.ui.ApeakMy;
import io.horizon.spi.web.Seeker;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.init.IxPin;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.mod.crud.uca.input.Pre;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.uca.cache.RapidKey;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicMy implements Agonic {

    /*
     * {
     *     "view": [view, position]
     *     "uri": "http path",
     *     "method": "http method",
     *     "sigma": "The application uniform",
     *     "resourceId": "Seeker result"
     * }
     */
    @Override
    public Future<JsonArray> runJAAsync(final JsonObject input, final IxMod in) {
        final UxJooq jooq = IxPin.jooq(in);
        return this.fetchResources(input, jooq, in)
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
            .compose(params -> this.fetchViews(params, jooq, in));
    }

    private Future<JsonObject> fetchResources(final JsonObject input, final UxJooq jooq, final IxMod in) {
        final String key = in.cacheKey() + ":" + input.hashCode();
        return Rapid.<String, JsonObject>t(RapidKey.RESOURCE, Agonic.EXPIRED).cached(key,
            () -> Ux.channel(Seeker.class, JsonObject::new, seeker -> seeker.on(jooq).fetchImpact(input)));
    }

    private Future<JsonArray> fetchViews(final JsonObject params, final UxJooq jooq, final IxMod in) {
        /*
         * To avoid the calculation for different module, here may cause performance issue
         * But I think it's valuable for open the cached of the personal.
         */
        return Ux.channel(ApeakMy.class, JsonArray::new, stub -> stub.on(jooq).fetchMy(params));
        //        final String key = in.cacheKey() + ":" + params.hashCode();
        //        final User user = in.envelop().user();
        //        return Rapid.<JsonArray>user(user, RapidKey.User.MY_VIEW).cached(key,
        //            () -> Ux.channel(ApeakMy.class, JsonArray::new, stub -> stub.on(jooq).fetchMy(params)));
    }
}
