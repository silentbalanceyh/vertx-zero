package io.vertx.up.backbone.router;

import io.horizon.specification.boot.HAxis;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.impl.Origin;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import io.vertx.up.configuration.BootStore;
import io.vertx.up.eon.KWeb;
import io.vertx.up.plugin.session.SessionClient;
import io.vertx.up.plugin.session.SessionInfix;
import io.vertx.up.secure.config.CorsConfig;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RouterAxis implements HAxis<Router> {

    private static final CorsConfig CONFIG = CorsConfig.get();

    private static final BootStore STORE = BootStore.singleton();

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
         * Timeout for blocked issue
         */
        // this.mountTimeout(router);
        /*
         * Body, Content
         */
        router.route().order(KWeb.ORDER.BODY).handler(BodyHandler.create().setBodyLimit(32 * MB));
        router.route().order(KWeb.ORDER.CONTENT).handler(ResponseContentTypeHandler.create());
        // 3. Cors data here
        this.mountCors(router);
    }

    private void mountTimeout(final Router router) {
        /*
         * 10s time out issue
         */
        router.route().order(KWeb.ORDER.TIMEOUT).handler(TimeoutHandler.create(10000L));
    }

    private void mountSession(final Router router) {
        if (STORE.isSession()) {
            /*
             * Session Global for Authorization, replace old mode with
             * SessionClient, this client will get SessionStore
             * by configuration information instead of create it directly.
             */
            final SessionClient client = SessionInfix.getOrCreate(this.vertx);
            router.route().order(KWeb.ORDER.SESSION).handler(client.getHandler());
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
            router.route().order(KWeb.ORDER.SESSION).handler(SessionHandler.create(store));
        }
    }

    /*
     * Fixed from 4.3.1
     */
    private void mountCors(final Router router) {
        final CorsHandler handler = CorsHandler.create()
            .allowCredentials(CONFIG.getCredentials())
            .allowedHeaders(this.getAllowedHeaders(CONFIG.getHeaders()))
            .allowedMethods(this.getAllowedMethods(CONFIG.getMethods()));
        /*
         * Allowed Multi origin in origin list
         */
        final JsonArray originArr = CONFIG.getOrigin();
        Ut.itJArray(originArr, String.class, (value, index) -> {
            if (Origin.isValid(value)) {
                handler.addOrigin(value);
            }
        });
        router.route().order(KWeb.ORDER.CORS).handler(handler);
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
            .map(Ut::toMethod)
            .forEach(methodSet::add);
        return methodSet;
    }
}
