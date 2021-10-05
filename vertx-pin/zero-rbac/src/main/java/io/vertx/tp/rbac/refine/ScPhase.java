package io.vertx.tp.rbac.refine;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.Orbit;
import io.vertx.tp.optic.Pocket;
import io.vertx.tp.optic.atom.Income;
import io.vertx.tp.rbac.permission.ScHabitus;
import io.vertx.up.atom.secure.Vis;
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
    private static final String LOGGER_VIEW = "( view = {1} ) Try cacheKey: \u001b[0;34m{0}\u001b[m, uri = {2}, method = {3}";

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
        final String uri = uri(context);
        /* Cache Data */
        final String literal = request.getParam(KName.VIEW);
        Sc.debugAuth(LOGGER, "Processed Uri: {0}", uri);
        final String cacheKey = Ke.keySession(request.method().name(), uri, Vis.create(literal));
        /* Cache Data */
        Sc.infoView(ScPhase.class, LOGGER_VIEW, cacheKey, literal, uri, request.method().name());
        return cacheKey;
    }

    static Future<JsonObject> cacheBound(final RoutingContext context, final Envelop envelop) {
        final String habit = envelop.token(KName.HABITUS);
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
