package io.vertx.tp.plugin.cache;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.l1.L1Cache;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Life Cycle for cache when processing in
 */
public class Harp {
    private static final Annal LOGGER = Annal.get(Harp.class);
    private static final String KEY_CACHE = "cache";
    private static final ConcurrentMap<String, HarpBus> POOL_BUS
            = new ConcurrentHashMap<>();
    private static Supplier<HarpBus> BUS_HARP;

    /*
     * First method for initialized and read configuration
     */
    public static void init(final Vertx vertx) {
        final Node<JsonObject> node = Ut.instance(ZeroUniform.class);
        final JsonObject options = node.read();
        if (options.containsKey(KEY_CACHE)) {
            /*
             * Cache enabled
             */
            final JsonObject config = options.getJsonObject(KEY_CACHE);
            LOGGER.info("( Cache ) L1,L2,L3 has been configured: {0}", config);
            BUS_HARP = () -> HarpBus.create(vertx, config);
        }
    }

    public static L1Cache cacheL1() {
        return Objects.isNull(BUS_HARP) ? null : Fn.poolThread(POOL_BUS, BUS_HARP).cacheL1();
    }
}
