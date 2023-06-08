package io.vertx.up.boot.anima;

import io.macrocosm.specification.config.HConfig;

/**
 * Child component works
 */
public interface Scatter<Vertx> {
    /**
     * Connect to vert.x to execute begin up works.
     *
     * @param vertx common vertx.
     */
    void connect(Vertx vertx, HConfig config);
}
