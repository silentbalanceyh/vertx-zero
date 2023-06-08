package io.vertx.up.backbone.router;

import io.vertx.ext.web.Route;
import io.vertx.up.atom.agent.Event;

/**
 * Hub for Uri basic
 * path, method, order
 * register to route to generate mime support
 */
public class UriHub implements Hub<Route> {

    @Override
    public void mount(final Route route,
                      final Event event) {
        if (null == event.getMethod()) {
            // Support filter JSR340
            route.path(event.getPath())
                .order(event.getOrder());
        } else {
            route.path(event.getPath())
                .method(event.getMethod())
                .order(event.getOrder());
        }
    }
}
