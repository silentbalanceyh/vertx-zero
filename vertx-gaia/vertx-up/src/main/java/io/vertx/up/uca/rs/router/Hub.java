package io.vertx.up.uca.rs.router;

import io.vertx.up.atom.agent.Event;

/**
 * Hub management for route
 */
public interface Hub<Route> {
    /**
     * Route mount
     *
     * @param route
     * @param event
     */
    void mount(Route route, Event event);
}
