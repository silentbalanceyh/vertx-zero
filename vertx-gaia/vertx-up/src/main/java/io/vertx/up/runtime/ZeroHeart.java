package io.vertx.up.runtime;

import io.horizon.eon.ZeroYml;
import io.horizon.eon.em.container.ServerType;
import io.vertx.core.ClusterOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.options.DynamicVisitor;
import io.vertx.up.uca.options.ServerVisitor;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/*
 * ZeroHeart 移除掉原始的 init 初始化类函数，只保留 is 检测函数
 * ZeroArcane 负责启动过程中生命周期管理以及组件初始化（新版）
 */
public class ZeroHeart {

    private static final Annal LOGGER = Annal.get(ZeroHeart.class);
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);

    /*
     * Shared Map
     */
    public static boolean isShared() {
        final ConcurrentMap<String, Class<?>> injections = ZeroAmbient.getInjections();
        return injections.containsKey(ZeroYml.inject.shared);
    }

    /*
     * Session
     */
    public static boolean isSession() {
        final JsonObject options = VISITOR.read();
        return options.containsKey(ZeroYml.inject.session);
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
