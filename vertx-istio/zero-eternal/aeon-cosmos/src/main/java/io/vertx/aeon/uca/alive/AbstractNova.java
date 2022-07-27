package io.vertx.aeon.uca.alive;

import io.vertx.aeon.specification.program.HNova;
import io.vertx.core.Vertx;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AbstractNova implements HNova {
    protected Vertx vertx;

    @Override
    @SuppressWarnings("unchecked")
    public HNova bind(final Vertx vertx) {
        this.vertx = vertx;
        return this;
    }
}
