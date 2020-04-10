package io.vertx.up;

import java.util.function.Consumer;

/**
 * Launcher:
 * 1. Cluster
 * 2. Vertx
 * 3. Verticle Deployment
 * 4. Router Deployment
 */
public interface Launcher<Vertx> {
    /**
     * Start
     */
    void start(Consumer<Vertx> vertx);

    /**
     * Stop
     */
    void stop(Consumer<Vertx> vertx);
}
