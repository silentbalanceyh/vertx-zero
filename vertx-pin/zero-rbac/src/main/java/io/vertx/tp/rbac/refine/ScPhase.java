package io.vertx.tp.rbac.refine;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.tp.ke.cv.KeDefault;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.Orbit;
import io.vertx.tp.optic.Pocket;
import io.vertx.tp.optic.atom.Income;
import io.vertx.tp.rbac.permission.ScHabitus;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/*
 * Uri Processing to calculate resource key here.
 */
class ScPhase {

    private static final Annal LOGGER = Annal.get(ScPhase.class);

    /*
     * To avoid two request
     */
    static String uri(final String uri, final String requestUri) {
        return Ke.channelSync(Orbit.class, () -> uri, orbit -> {
            /* Pocket processing */
            final Income income = Pocket.income(Orbit.class, uri, requestUri);
            return orbit.analyze(income.arguments());
        });
    }

    static String uri(final RoutingContext context) {
        final HttpServerRequest request = context.request();
        final HttpMethod method = request.method();
        final String requestUri = ZeroAnno.recoveryUri(request.path(), method);
        return uri(requestUri, request.path());
    }


    private static String cacheKey(final RoutingContext context) {
        final HttpServerRequest request = context.request();
        String uri = uri(context);
        /* Cache Data */
        Sc.debugAuth(LOGGER, "Processed Uri: {0}", uri);
        /* Cache Key */
        String view = request.getParam(KName.VIEW);
        if (Ut.isNil(view)) {
            view = KeDefault.VIEW_DEFAULT;
        }
        uri = uri + ":" + view;
        final String cacheKey = Ke.keySession(request.method().name(), uri);
        /* Cache Data */
        Sc.infoView(ScPhase.class, "Try cacheKey: {0}", cacheKey);
        return cacheKey;
    }

    static Future<JsonObject> cacheBound(final RoutingContext context, final Envelop envelop) {
        final String habit = Ke.keyHabitus(envelop);
        if (Ut.isNil(habit)) {
            /*
             * Empty bound in current interface instead of other
             */
            return Ux.future(new JsonObject());
        } else {
            /*
             * ScHabitus instead of Session
             */
            final ScHabitus habitus = ScHabitus.initialize(habit);
            /*
             * Cache key
             */
            final String cacheKey = cacheKey(context);
            return habitus.get(cacheKey);
        }
    }
}
