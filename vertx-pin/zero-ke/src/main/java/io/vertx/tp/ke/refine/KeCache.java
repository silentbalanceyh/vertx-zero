package io.vertx.tp.ke.refine;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.tp.optic.Orbit;
import io.vertx.tp.optic.Pocket;
import io.vertx.tp.optic.atom.Income;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroAnno;

/*
 * Key generated for uniform platform
 */
class KeCache {

    private static final Annal LOGGER = Annal.get(KeCache.class);
    private static final String LOGGER_VIEW = "( view = {1} ) Try cacheKey: \u001b[0;34m{0}\u001b[m, uri = {2}, method = {3}";

    static String keyView(final String method, final String uri, final Vis view) {
        /*
         * session-POST:uri:position/name
         */
        return "session-" + method + ":" + uri + ":" + view.position() + "/" + view.view();
    }

    static String keyAuthorized(final String method, final String uri) {
        return "authorized-" + method + ":" + uri;
    }

    static String keyResource(final String method, final String uri) {
        return "resource-" + method + ":" + uri;
    }

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

    static String keyView(final RoutingContext context) {
        final HttpServerRequest request = context.request();
        final String uri = uri(context);
        /* Cache Data */
        final String literal = request.getParam(KName.VIEW);
        Ke.infoKe(LOGGER, "Processed Uri: {0}", uri);
        final String cacheKey = keyView(request.method().name(), uri, Vis.create(literal));
        /* Cache Data */
        Ke.infoKe(LOGGER, LOGGER_VIEW, cacheKey, literal, uri, request.method().name());
        return cacheKey;
    }
}
