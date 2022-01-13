package io.vertx.up.runtime;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Plugins;
import io.vertx.up.eon.em.ServerType;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.options.DynamicVisitor;
import io.vertx.up.uca.options.ServerVisitor;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class ZeroHeart {

    private static final Annal LOGGER = Annal.get(ZeroHeart.class);
    private static final String INIT = "init";
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);

    public static void initExtension() {
        initExtension(null).onComplete(res -> LOGGER.info("Extension Initialized {0}", res.result()));
    }

    /*
     * Async initialized for extension
     */
    public static Future<Boolean> initExtension(final Vertx vertx) {
        // inject configuration
        final JsonObject config = VISITOR.read();
        /*
         * Check whether there exist `init` node for class
         * Each `init` clazz must be configured as
         * init:
         * - clazz: XXXX
         *   config:
         *      key1:value1
         *      key2:value2
         */
        if (config.containsKey(INIT)) {
            /* Component initializing with native */
            final JsonArray components = config.getJsonArray(INIT, new JsonArray());
            LOGGER.info("Extension components initialized {0}", components.encode());
            return Ux.nativeInit(components, vertx);
        } else {
            LOGGER.info("Extension configuration missing {0}", config);
            return Future.succeededFuture(Boolean.TRUE);
        }
    }

    /*
     * Shared Map
     */
    public static boolean isShared() {
        final ConcurrentMap<String, Class<?>> injections = ZeroAmbient.getInjections();
        return injections.containsKey(Plugins.Infix.SHARED);
    }

    /*
     * Session
     */
    public static boolean isSession() {
        final JsonObject options = VISITOR.read();
        return options.containsKey(Plugins.Infix.SESSION);
    }

    /*
     * Micro
     */
    public static boolean isEtcd() {
        final JsonObject options = VISITOR.read();
        return options.containsKey("etcd");
    }

    /*
     * Cache
     */
    public static boolean isCache() {
        final JsonObject options = VISITOR.read();
        return options.containsKey("cache");
    }

    /*
     * Whether Current node is ApiGateway
     */
    public static boolean isGateway() {
        /*
         * This method is only ok when `micro` mode
         * Secondary Scanned for Api Gateway
         */
        final Set<Integer> apiScanned = new HashSet<>();
        Fn.outUp(() -> {
            final ServerVisitor<HttpServerOptions> visitor =
                Ut.singleton(DynamicVisitor.class);
            apiScanned.addAll(visitor.visit(ServerType.API.toString()).keySet());
        }, LOGGER);
        return !apiScanned.isEmpty();
    }
}
