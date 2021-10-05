package io.vertx.tp.ke.refine;

import io.vertx.tp.plugin.session.SessionClient;
import io.vertx.tp.plugin.session.SessionInfix;
import io.vertx.up.atom.secure.Vis;

/*
 * Key generated for uniform platform
 */
class KeCache {

    private static final SessionClient CLIENT = SessionInfix.getClient();

    static String keySession(final String method, final String uri, final Vis view) {
        /*
         * session-POST:uri:position/name
         */
        return "session-" + method + ":" + uri + ":" + view.position() + "/" + view.view();
    }

    static String keyAuthorized(final String method, final String uri) {
        return "authorized-" + method + ":" + uri;
    }
}
