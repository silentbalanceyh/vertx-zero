package io.vertx.tp.plugin.cache;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.l1.L1Cache;
import io.vertx.tp.plugin.cache.l1.L1Config;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class HarpBus {
    private static final String KEY_L1 = "l1";
    private transient L1Cache l1;

    private HarpBus(final Vertx vertx, final JsonObject options) {
        /*
         * L1 processing
         */
        this.initL1(vertx, options.getJsonObject(KEY_L1));
    }

    static HarpBus create(final Vertx vertx, final JsonObject options) {
        return new HarpBus(vertx, options);
    }

    private void initL1(final Vertx vertx, final JsonObject config) {
        final L1Config configL1 = Ut.deserialize(config, L1Config.class);
        if (Objects.nonNull(configL1.getComponent())) {
            /*
             * L1 cache here
             */
            final Class<?> cacheClass = configL1.getComponent();
            if (Ut.isImplement(cacheClass, L1Cache.class)) {
                final L1Cache cache = Ut.instance(configL1.getComponent());
                cache.bind(vertx).bind(configL1);
                this.l1 = cache;
            }
        }
    }

    L1Cache cacheL1() {
        return this.l1;
    }
}
