package io.vertx.tp.plugin.cache.hit;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.l1.L1Cache;
import io.vertx.tp.plugin.cache.l1.L1Config;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.log.Annal;
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

    /*
     * Refresh will send data to event bus
     * Publish / Consume mode for cache data processing
     * Actual the cache will be deleted
     */
    @Override
    public <T> void write(final CMessage message, final ChangeFlag flag) {
        /*
         * Address processing
         */
        final String address = this.config.getAddress();
        if (Ut.notNil(address)) {
            final EventBus eventBus = this.vertx.eventBus();
            /*
             * Delivery Message extraction
             */
            eventBus.publish(address, message.dataDelivery(flag));
        }
    }

    @Override
    public <T> Future<T> readAsync(final CMessage message) {
        // Get key
        final String uk = message.dataUnique();
        this.logger().info("( Cache ) L1 reader will read data by `{0}` ", uk);
        return this.readAsync(uk).compose(response -> {
            final T ret = Ut.deserialize(response, message.dataType());
            return Future.succeededFuture(ret);
        });
    }

    @Override
    public <T> T read(final CMessage message) {
        // Get key
        final String uk = message.dataUnique();
        this.logger().info("( Cache ) L1 reader will read data by `{0}` ", uk);
        final JsonObject data = this.read(uk);
        if (Ut.isNil(data)) {
            return null;
        } else {
            return Ut.deserialize(data, message.dataType());
        }
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    public abstract Future<JsonObject> readAsync(String key);

    public abstract JsonObject read(String key);
}
