package io.vertx.mod.ke.refine;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.commune.secure.Vis;

/**
 * @author lang : 2023-06-07
 */
class _Key {
    protected _Key() {
    }

    public static String keyView(final String method, final String uri, final Vis view) {
        return KeCache.keyView(method, uri, view);
    }

    public static String keyView(final RoutingContext context) {
        return KeCache.keyView(context);
    }

    public static String keyAuthorized(final String method, final String uri) {
        return KeCache.keyAuthorized(method, uri);
    }

    public static String keyResource(final String method, final String uri) {
        return KeCache.keyResource(method, uri);
    }

}
