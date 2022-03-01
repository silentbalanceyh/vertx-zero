package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.ui.ApeakMy;
import io.vertx.tp.optic.web.Seeker;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.uca.cache.RapidKey;
import io.vertx.up.uca.jooq.UxJooq;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicMy implements Agonic {
    private static final Annal LOGGER = Annal.get(AgonicMy.class);

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
            () -> Ke.channel(Seeker.class, JsonObject::new, seeker -> seeker.on(jooq).fetchImpact(input)));
    }

    private Future<JsonArray> fetchViews(final JsonObject params, final UxJooq jooq, final IxMod in) {
        final String key = in.cacheKey() + ":" + params.hashCode();
        final User user = in.envelop().user();
        return Rapid.<JsonArray>user(user, RapidKey.User.MY_VIEW).cached(key,
            () -> Ke.channel(ApeakMy.class, JsonArray::new, stub -> stub.on(jooq).fetchMy(params)));
    }
}
