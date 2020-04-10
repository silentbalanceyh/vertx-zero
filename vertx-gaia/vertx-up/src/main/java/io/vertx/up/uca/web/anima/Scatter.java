package io.vertx.up.uca.web.anima;

/**
 * Child component works
 */
public interface Scatter<Vertx> {
    /**
     * Connect to vert.x to execute begin up works.
     *
     * @param vertx common vertx.
     */
    void connect(Vertx vertx);
}
