package io.vertx.mod.ke.refine;

import io.aeon.atom.channel.KIncome;
import io.aeon.runtime.channel.Pocket;
import io.horizon.spi.web.Orbit;
import io.horizon.uca.log.Annal;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.commune.secure.Vis;
import io.vertx.up.eon.KName;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.unity.Ux;

import static io.vertx.mod.ke.refine.Ke.LOG;

/*
 * Key generated for uniform platform
 */
class KeCache {

    private static final Annal LOGGER = Annal.get(KeCache.class);
    private static final String LOGGER_VIEW = "Input view = {1}, Try cacheKey: \u001b[0;34m{0}\u001b[m, uri = {2}, method = {3}";

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
        return Ux.channelS(Orbit.class, () -> uri, orbit -> {
            /* Pocket processing */
            final KIncome income = Pocket.income(Orbit.class, uri, requestUri);
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
        /* Url Encoding / Decoding */
        final Vis vis = Vis.create(literal);
        final String cacheKey = keyView(request.method().name(), uri, vis);
        /* Cache Data */
        LOG.Ke.debug(LOGGER, LOGGER_VIEW, cacheKey, literal, uri, request.method().name());
        return cacheKey;
    }
}
