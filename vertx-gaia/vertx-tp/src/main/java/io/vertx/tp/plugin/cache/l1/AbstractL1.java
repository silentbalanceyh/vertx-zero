package io.vertx.tp.plugin.cache.l1;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.tp.plugin.cache.hit.HMeta;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;


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

    @Override
    public <T> void flushAsync(final T input, final ChangeFlag type, final HMeta meta) {
        /*
         * Address processing
         */
        final String address = this.config.getAddress();
        if (Ut.notNil(address)) {
            final EventBus eventBus = this.vertx.eventBus();
            /*
             * Delivery options
             */
            eventBus.publish(address, L1Kit.dataDelivery(input, type, meta));
        }
    }
}
