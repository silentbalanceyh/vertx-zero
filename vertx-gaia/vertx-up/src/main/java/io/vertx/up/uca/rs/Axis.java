package io.vertx.up.uca.rs;

/**
 * Axis to mount entity
 * 1. Router
 * 2. Route
 * 3. Event
 */
public interface Axis<Router> {
    /**
     * Router management entry
     *
     * @param router The router reference that will be stored
     */
    void mount(Router router);
}
