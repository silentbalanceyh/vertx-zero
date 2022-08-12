package io.vertx.up.runtime;

import io.vertx.aeon.eon.HPath;
import io.vertx.aeon.eon.em.TypeOs;
import io.vertx.aeon.refine.HLog;
import io.vertx.core.ClusterOptions;
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

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class ZeroHeart {

    private static final Annal LOGGER = Annal.get(ZeroHeart.class);
    private static final String INIT = "init";
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);

    @SuppressWarnings("all")
    public static void initEnvironment() {
        final TypeOs osType = TypeOs.from(System.getProperty("os.name"));
        Fn.safeJvm(() -> {
            if (Ut.ioExist(HPath.ENV_DEVELOPMENT)) {
                HLog.warnAeon(ZeroHeart.class, "OS = {0},  `{1}` file has been found! DEVELOPMENT connected.",
                    osType.name(), HPath.ENV_DEVELOPMENT);
                if (TypeOs.MAC_OS == osType) {
                    final Properties properties = Ut.ioProperties(HPath.ENV_DEVELOPMENT);
                    final Enumeration<String> it = (Enumeration<String>) properties.propertyNames();
                    final StringBuilder builder = new StringBuilder();
                    while (it.hasMoreElements()) {
                        final String key = it.nextElement();
                        final String value = properties.getProperty(key);
                        // .env.development （环境变量黑科技）
                        final Map<String, String> env = System.getenv();
                        final Field field = env.getClass().getDeclaredField("m");
                        field.setAccessible(true);
                        builder.append("\n\t").append(key).append(" = ").append(value);
                        ((Map<String, String>) field.get(env)).put(key, value);
                    }
                    HLog.infoAeon(ZeroHeart.class, "Zero Environment Variables: {0}\n", builder.toString());
                }
            }
        });
    }

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

    public static boolean isCluster() {
        final ClusterOptions cluster = ZeroGrid.getClusterOption();
        return cluster.isEnabled();
    }
}
