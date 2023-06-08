package io.vertx.up;

import io.horizon.eon.VMessage;
import io.horizon.specification.boot.HLauncher;
import io.horizon.uca.log.Log;
import io.macrocosm.specification.boot.HOff;
import io.macrocosm.specification.boot.HOn;
import io.macrocosm.specification.config.HConfig;
import io.vertx.core.ClusterOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EnvelopCodec;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.up.boot.options.VertxSetUp;
import io.vertx.up.commune.Envelop;
import io.vertx.up.runtime.ZeroOption;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * @author lang : 2023-05-30
 */
public class ZeroLauncher implements HLauncher<Vertx> {

    private static final ConcurrentMap<String, Vertx> VERTX = new ConcurrentHashMap<>();

    private static Vertx VERTX_NATIVE;

    public static Vertx nativeRef() {
        if (Objects.isNull(VERTX_NATIVE)) {
            VERTX_NATIVE = Vertx.vertx(VertxSetUp.nativeOption());
            final EventBus eventBus = VERTX_NATIVE.eventBus();
            eventBus.registerDefaultCodec(Envelop.class, Ut.singleton(EnvelopCodec.class));
        }
        return VERTX_NATIVE;
    }

    @Override
    public ConcurrentMap<String, Vertx> store() {
        return VERTX;
    }

    @Override
    public <T extends HConfig> void start(final HOn<T> on, final Consumer<Vertx> server) {
        // 直接提取参数查看是否集群模式
        final ClusterOptions clusterOptions = ZeroOption.getClusterOption();
        final ConcurrentMap<String, VertxOptions> vertxOptions = ZeroOption.getVertxOptions();
        if (clusterOptions.isEnabled()) {
            // 集群模式
            final ClusterManager manager = clusterOptions.getManager();
            Log.info(this.getClass(), VMessage.HLauncher.CLUSTER,
                manager.getClass().getName(), manager.getNodeId(), manager.isActive());
            vertxOptions.forEach((name, option) -> Vertx.clusteredVertx(option, cluster -> {
                // 启动实例
                final Vertx vertx = cluster.result();
                this.startInstance(name, vertx);
                // 回调执行
                server.accept(vertx);
            }));
        } else {
            // 单机模式
            vertxOptions.forEach((name, option) -> {
                final Vertx vertx = Vertx.vertx(option);
                // 启动实例
                this.startInstance(name, vertx);
                // 回调执行
                server.accept(vertx);
            });
        }
    }

    @Override
    public <T extends HConfig> void stop(final HOff<T> off, final Consumer<Vertx> server) {
        // 等待实现
    }

    private void startInstance(final String name, final Vertx vertx) {
        // 1. 注册编码器到 EventBut
        final EventBus eventBus = vertx.eventBus();
        eventBus.registerDefaultCodec(Envelop.class, Ut.singleton(EnvelopCodec.class));
        // 2. 填充名称
        VERTX.putIfAbsent(name, vertx);
    }
}
