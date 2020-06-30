package io.vertx.up.runtime.soul;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.up.fn.Fn;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 *
 * The soul of zero framework to store critical data / extract data
 */
public class UriAeon {
    /*
     * Thread -> Uri storage for dynamic routing deployment
     */
    private static final ConcurrentMap<String, UriNeuro> NEURO = new ConcurrentHashMap<>();
    private static final UriStore STORE = UriStore.create();

    /*
     * Registry route to ZeroAeon
     */
    public static void connect(final Router router) {
        /*
         * Initialize the routing system to store reference
         */
        final String threadId = Thread.currentThread().getName();
        Fn.poolThread(NEURO, () -> UriNeuro.getInstance(threadId).bind(router));
    }


    /*
     * Routing mounting here ( Dynamic Modification )
     */
    public static void mountRoute(final JsonObject config) {
        /*
         * Create new routing on `original` route object
         */
        NEURO.values().forEach(neuro -> neuro.addRoute(config));
    }

    /*
     * ADD / SEARCH / GET
     */
    // ADD
    public static void uriAdd(final UriMeta meta) {
        if (Objects.nonNull(meta)) {
            STORE.add(meta);
        }
    }

    // SEARCH
    public static List<UriMeta> uriSearch(final String keyword) {
        return STORE.search(keyword);
    }

    // GET
    public static List<UriMeta> uriGet() {
        return STORE.getAll();
    }
}