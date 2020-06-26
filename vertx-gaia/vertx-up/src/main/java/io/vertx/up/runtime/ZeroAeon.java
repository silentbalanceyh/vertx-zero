package io.vertx.up.runtime;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.soul.UriNeuro;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 *
 * The soul of zero framework to store critical data / extract data
 */
public class ZeroAeon {
    /*
     * Thread -> Uri storage for dynamic routing deployment
     */
    private static final ConcurrentMap<String, UriNeuro> NEURO = new ConcurrentHashMap<>();

    /*
     * Registry route to ZeroAeon
     */
    public static void initialize(final Router router) {
        /*
         * Initialize the routing system to store reference
         */
        final String threadId = Thread.currentThread().getName();
        Fn.poolThread(NEURO, () -> UriNeuro.getInstance(threadId).bind(router));
    }

    /*
     * Routing mounting here
     */
    public static void addRoute(final JsonObject config) {
        /*
         * Create new routing on `original` route object
         */
        NEURO.values().forEach(neuro -> neuro.addRoute(config));
    }
}