package io.vertx.tp.plugin.cache.l1;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.hit.CacheKey;
import io.vertx.tp.plugin.cache.hit.CacheMeta;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Collection;


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
    public <T> void write(final T input, final ChangeFlag flag, final CacheMeta meta) {
        /*
         * Address processing
         */
        final String address = this.config.getAddress();
        if (Ut.notNil(address)) {
            final EventBus eventBus = this.vertx.eventBus();
            /*
             * Delivery options
             */
            final L1Algorithm algorithm;
            if (input instanceof Collection) {
                /*
                 * Collection
                 */
                algorithm = Ut.singleton(AlgorithmCollection.class);
            } else {
                /*
                 * Record
                 */
                algorithm = Ut.singleton(AlgorithmRecord.class);
            }
            eventBus.publish(address, algorithm.dataDelivery(input, flag, meta));
        }
    }

    @Override
    public <T> Future<T> readAsync(final CacheKey key, final CacheMeta meta) {
        // Get key
        final String uk = key.unique(meta);
        this.logger().info("( Cache ) L1 reader will read data by `{0}` ", uk);
        return this.readAsync(uk).compose(response -> {
            final T ret = Ut.deserialize(response, meta.type());
            return Future.succeededFuture(ret);
        });
    }

    @Override
    public <T> T read(final CacheKey key, final CacheMeta meta) {
        // Get key
        final String uk = key.unique(meta);
        this.logger().info("( Cache ) L1 reader will read data by `{0}` ", uk);
        final JsonObject data = this.read(uk);
        if (Ut.isNil(data)) {
            return null;
        } else {
            return Ut.deserialize(data, meta.type());
        }
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    public abstract Future<JsonObject> readAsync(String key);

    public abstract JsonObject read(String key);
}
