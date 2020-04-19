package io.vertx.up.uca.rs.router;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.ResponseContentTypeHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import io.vertx.tp.plugin.session.SessionClient;
import io.vertx.tp.plugin.session.SessionInfix;
import io.vertx.up.eon.Orders;
import io.vertx.up.runtime.ZeroHeart;
import io.vertx.up.secure.config.CorsConfig;
import io.vertx.up.uca.rs.Axis;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RouterAxis implements Axis<Router> {

    private static final CorsConfig CONFIG = CorsConfig.get();

    private static final int KB = 1024;
    private static final int MB = KB * 1024;

    private transient final Vertx vertx;

    public RouterAxis(final Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void mount(final Router router) {
        // 1. Cookie, Body
        // Enabled by default
        // router.route().order(Orders.COOKIE).handler(CookieHandler.create());
        // 2. Session
        /*
         * CSRF Handler Setting ( Disabled in default )
         */
        this.mountSession(router);
        /*
         * Body, Content
         */
        router.route().order(Orders.BODY).handler(BodyHandler.create().setBodyLimit(32 * MB));
        router.route().order(Orders.CONTENT).handler(ResponseContentTypeHandler.create());
        // 3. Cors data here
        this.mountCors(router);
    }

    private void mountSession(final Router router) {
        if (ZeroHeart.isSession()) {
            /*
             * Session Global for Authorization, replace old mode with
             * SessionClient, this client will get SessionStore
             * by configuration information instead of create it directly.
             */
            final SessionClient client = SessionInfix.getOrCreate(this.vertx);
            router.route().order(Orders.SESSION).handler(client.getHandler());
        } else {
            /*
             * Default Session Handler here for public domain
             * If enabled session extension, you should provide other session store
             */
            final SessionStore store;
            if (this.vertx.isClustered()) {
                /*
                 * Cluster environment
                 */
                store = ClusteredSessionStore.create(this.vertx);
            } else {
                /*
                 * Single Node environment
                 */
                store = LocalSessionStore.create(this.vertx);
            }
            router.route().order(Orders.SESSION).handler(SessionHandler.create(store));
        }
    }

    private void mountCors(final Router router) {
        router.route().order(Orders.CORS).handler(CorsHandler.create(CONFIG.getOrigin())
                .allowCredentials(CONFIG.getCredentials())
                .allowedHeaders(this.getAllowedHeaders(CONFIG.getHeaders()))
                .allowedMethods(this.getAllowedMethods(CONFIG.getMethods())));
    }

    private Set<String> getAllowedHeaders(final JsonArray array) {
        final Set<String> headerSet = new HashSet<>();
        array.stream()
                .filter(Objects::nonNull)
                .map(item -> (String) item)
                .forEach(headerSet::add);
        return headerSet;
    }

    private Set<HttpMethod> getAllowedMethods(final JsonArray array) {
        final Set<HttpMethod> methodSet = new HashSet<>();
        array.stream()
                .filter(Objects::nonNull)
                .map(item -> (String) item)
                .map(item -> Ut.toEnum(() -> item, HttpMethod.class, HttpMethod.GET))
                .forEach(methodSet::add);
        return methodSet;
    }
}
