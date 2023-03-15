package io.vertx.up.uca.rs.router;

import io.vertx.up.atom.agent.Event;

/**
 * Hub management for route
 */
public interface Hub<Route> {
    /**
     * Route mount
     *
     * @param route The route reference
     * @param event The route configuration data object
     */
    void mount(Route route, Event event);
}
