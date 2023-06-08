package io.vertx.up.uca.soul;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.up.eon.KWeb;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 *
 * The data structure to store following data ( Each thread )
 */
public class UriNeuro {
    private final transient String name;
    private transient Router router;

    private UriNeuro(final String name) {
        this.name = name;
    }

    public static UriNeuro getInstance(final String name) {
        return new UriNeuro(name);
    }

    public UriNeuro bind(final Router router) {
        this.router = router;
        return this;
    }

    /*
     * Add new Routing to current routers
     * 1) This feature could add routing dynamic instead of static management
     * 2) The method is only for deployment here
     */
    public void addRoute(final JsonObject data) {
        final Route route = this.router.route();
        route.path(data.getString("uri"));
        route.order(KWeb.ORDER.EVENT);
        route.handler(item -> {
            System.out.println("Hello World!");
        });
    }
}
