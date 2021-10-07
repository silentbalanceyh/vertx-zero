package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.ApeakMy;
import io.vertx.tp.optic.Seeker;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.KValue;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.UxPool;

import java.util.Objects;

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
        final UxPool pool = Ux.Pool.on(Constants.Pool.CACHE_RESOURCE);
        return pool.<String, JsonObject>get(key).compose(resources -> {
            if (Objects.isNull(resources)) {
                return Ke.channel(Seeker.class, JsonObject::new, seeker -> seeker.on(jooq).fetchImpact(input))
                    .compose(resourceData -> pool.put(key, resourceData, Agonic.EXPIRED).compose(nil -> Ux.future(resourceData)));
            } else {
                LOGGER.info("[ My ] Resource cached hit by {0}", key);
                return Ux.future(resources);
            }
        });
    }

    private Future<JsonArray> fetchViews(final JsonObject params, final UxJooq jooq, final IxMod in) {
        final Envelop envelop = in.envelop();
        final String habitus = envelop.habitus();
        final UxPool pool = Ux.Pool.on(habitus);

        return pool.<String, JsonObject>get(KValue.View.MY_CACHE).compose(views -> {
            final String key = in.cacheKey() + ":" + params.hashCode();
            if (Objects.isNull(views)) {
                views = new JsonObject();
            }
            if (views.containsKey(key)) {
                LOGGER.info("[ My ] View cached hit by {0}", key);
                return Ux.future(views.getJsonArray(key));
            } else {
                final JsonObject finalViews = views;
                return Ke.channel(ApeakMy.class, JsonArray::new, stub -> stub.on(jooq).fetchMy(params))
                    .compose(viewData -> {
                        finalViews.put(key, viewData);
                        return pool.put(KValue.View.MY_CACHE, finalViews).compose(nil -> Ux.future(viewData));
                    });
            }
        });
    }
}
