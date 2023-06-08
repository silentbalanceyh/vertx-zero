package io.vertx.up.plugin.cache;

import io.horizon.uca.cache.Cc;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.plugin.cache.l1.L1Cache;
import io.vertx.up.plugin.cache.l1.L1Config;
import io.vertx.up.uca.log.DevOps;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HarpBus {
    private static final ConcurrentMap<String, L1Cache> POOL_L1
        = new ConcurrentHashMap<>();

    private static final Cc<String, L1Cache> CC_L1 = Cc.openThread();
    private final transient Vertx vertx;
    private L1Config l1Config;

    private HarpBus(final Vertx vertx, final JsonObject options) {
        this.vertx = vertx;
        /*
         * L1 processing
         */
        this.initL1(vertx, options.getJsonObject(YmlCore.cache.L1));
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
            vertx.deployVerticle(worker.getName(), workerOptions, result -> {
                if (result.succeeded()) {
                    DevOps.on(vertx).add(worker.getName(), workerOptions, result.result());
                } else {
                    if (null != result.cause()) {
                        result.cause().printStackTrace();
                    }
                }
            });
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
            cache = CC_L1.pick(() -> {
                final L1Cache created = Ut.instance(cacheClass);
                return created.bind(this.vertx).bind(this.l1Config.copy());
            });
            //            cache = Fn.po?lThread(POOL_L1, () -> {
            //                final L1Cache created = Ut.instance(cacheClass);
            //                return created.bind(this.vertx).bind(this.l1Config.copy());
            //            });
        }
        return cache;
    }
}
