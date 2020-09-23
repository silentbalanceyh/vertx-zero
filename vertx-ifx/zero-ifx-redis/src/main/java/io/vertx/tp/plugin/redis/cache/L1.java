package io.vertx.tp.plugin.redis.cache;

import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.hit.HKey;
import io.vertx.tp.plugin.cache.l1.AbstractL1;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class L1 extends AbstractL1 {
    private transient final L1Channel channel;

    public L1() {
        this.channel = new L1Channel();
    }


    @Override
    public <T> void flushAsync(final T input, final ChangeFlag type, final Class<T> clazz) {
        /*
         * Address processing
         */
        final String address = this.config.getAddress();
        if (Ut.notNil(address)) {
            final EventBus eventBus = this.vertx.eventBus();
            /*
             * Delivery options
             */
            eventBus.publish(address, L1Kit.dataDelivery(input, type, clazz));
        }
    }

    @Override
    public <T> Future<T> hitAsync(final HKey key, final Class<T> clazz) {
        // Get key
        final String uk = key.unique();
        return this.channel.hitAsync(uk).compose(response -> {
            final T ret = Ut.deserialize(response, clazz);
            return Future.succeededFuture(ret);
        });
    }

    @Override
    public <T> T hit(final HKey key, final Class<T> clazz) {
        // Get key
        final String uk = key.unique();
        // Data hit
        final JsonObject data = this.channel.hit(uk);
        if (Ut.isNil(data)) {
            return null;
        } else return Ut.deserialize(data, clazz);
    }
}
