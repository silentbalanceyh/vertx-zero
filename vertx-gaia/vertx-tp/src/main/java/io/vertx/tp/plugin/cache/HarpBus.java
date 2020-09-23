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
    private final transient Vertx vertx;
    private transient L1Cache l1;

    private HarpBus(final Vertx vertx, final JsonObject options) {
        this.vertx = vertx;
        /*
         * L1 processing
         */
        final L1Config config = Ut.deserialize(options.getJsonObject(KEY_L1), L1Config.class);
        if (Objects.nonNull(config.getComponent())) {
            /*
             * L1 cache here
             */
            final L1Cache cache = Ut.instance(config.getComponent());
            cache.bind(vertx).bind(config);
        }
    }

    static HarpBus create(final Vertx vertx, final JsonObject options) {
        return new HarpBus(vertx, options);
    }

    public void subscribe() {

    }
}
