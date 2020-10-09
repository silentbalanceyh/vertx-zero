package io.vertx.tp.plugin.cache;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.l1.L1Cache;
import io.vertx.tp.plugin.cache.l1.L1Config;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class HarpBus {
    private static final String KEY_L1 = "l1";
    private static final ConcurrentMap<String, L1Cache> POOL_L1
            = new ConcurrentHashMap<>();
    private final transient Vertx vertx;
    private L1Config l1Config;

    private HarpBus(final Vertx vertx, final JsonObject options) {
        this.vertx = vertx;
        /*
         * L1 processing
         */
        this.initL1(vertx, options.getJsonObject(KEY_L1));
    }

    static HarpBus create(final Vertx vertx, final JsonObject options) {
        return new HarpBus(vertx, options);
    }

    private void initL1(final Vertx vertx, final JsonObject options) {
        this.l1Config = Ut.deserialize(options, L1Config.class);
        final Class<?> worker = this.l1Config.getWorker();
        if (Objects.nonNull(worker) && Ut.isImplement(worker, Verticle.class)) {
            /*
             * Address, Class
             */
            final DeploymentOptions workerOptions = new DeploymentOptions();
            workerOptions.setWorker(true);
            /*
             * Configuration
             */
            workerOptions.setConfig(options.copy());
            vertx.deployVerticle(worker.getName(), workerOptions);
        }
    }

    L1Cache cacheL1() {
        L1Cache cache = null;
        final Class<?> componentCls = this.l1Config.getComponent();
        if (Objects.nonNull(componentCls) && Ut.isImplement(componentCls, L1Cache.class)) {
            /*
             * L1 cache here
             */
            final Class<?> cacheClass = this.l1Config.getComponent();
            cache = Fn.poolThread(POOL_L1, () -> {
                final L1Cache created = Ut.instance(cacheClass);
                return created.bind(this.vertx).bind(this.l1Config.copy());
            });
        }
        return cache;
    }
}
