package io.vertx.tp.plugin.cache.l1;

import io.vertx.core.Vertx;


/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Abstract class for uniform processing for L1 cache
 */
public abstract class AbstractL1 implements L1Cache {
    protected transient Vertx vertx;
    protected transient L1Config config;

    /*
     * vertxRef reference for
     */
    @Override
    public L1Cache bind(final L1Config config) {
        this.config = config;
        return this;
    }

    @Override
    public L1Cache bind(final Vertx vertx) {
        this.vertx = vertx;
        return this;
    }
}
