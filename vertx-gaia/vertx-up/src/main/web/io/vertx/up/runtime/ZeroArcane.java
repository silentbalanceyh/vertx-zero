package io.vertx.up.runtime;

import io.horizon.uca.boot.KPivot;
import io.macrocosm.specification.boot.HOn;
import io.macrocosm.specification.config.HConfig;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.configuration.BootStore;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.plugin.shared.MapInfix;
import io.vertx.up.unity.Ux;

import java.util.Set;
import java.util.function.BiConsumer;

import static io.aeon.refine.Ho.LOG;

/**
 * Arcane:神秘的，秘密的
 * Zero新版启动器，新的初始化流程发生了变动，直接更改成了如下流程
 * <pre><code>
 *     1. {@link io.horizon.uca.boot.KLauncher} 构造
 *        内部流程：
 *        - {@link io.horizon.spi.BootIo} SPI 连接
 *        - 初始化环境变量
 *        - 提取 {@link io.macrocosm.specification.boot.HOn} 构造配置扫描器
 *        - 执行 {@link HOn#configure} 初始化 {@link io.macrocosm.specification.config.HConfig}
 *     2. 完成 WebServer 的创建过程（此处 WebServer 是主框架的容器实例）
 *        - Zero 中 Vertx 实例
 *        - OSGI 中 Framework 实例
 * </code></pre>
 * 上述步骤完成后调用此类中的方法，做静态启动流程（此处启动流程为共享流程，依旧处于初始化阶段）
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ZeroArcane {
    private static final String MSG_EXT_COMPONENT = "Extension components initialized {0}";
    private static final String MSG_EXT_CONFIGURATION = "Extension configuration missing {0}";
    private static final BootStore STORE = BootStore.singleton();

    public static void start(final Vertx vertx, final HConfig config,
                             final BiConsumer<Vertx, HConfig> consumer) {
        startElite(vertx, config);

        startEnroll(vertx, config)
            .compose(ark -> startEdge(vertx, config, ark))
            .onSuccess(initialized -> {
                if (initialized) {
                    // 启动核心组件
                    consumer.accept(vertx, config);
                } else {
                    // 异常退出
                    System.exit(1);
                }
            }).onFailure(error -> {
                error.printStackTrace();
                // 异常退出
                System.exit(1);
            });
    }

    /**
     * 「同步流程：Vertx启动后」启动流程二
     * Elite：精华
     * <p>
     * 流程一：Vertx原生插件初始化，带Vertx的专用启动流程，在Vertx实例启动之后启动
     * <pre><code>
     *         1. SharedMap提前初始化（Infix架构下所有组件的特殊组件预启动流程）
     *         2. 其他Native插件初始化
     *     </code></pre>
     * </p>
     *
     * @param vertx  Vertx实例
     * @param config 启动配置
     */
    private static void startElite(final Vertx vertx, final HConfig config) {
        if (STORE.isShared()) {
            /*
             * Map infix initialized first to fix
             * Boot issue here to enable map infix ( SharedMap will be used widely )
             * It means that the MapInfix should started twice for safe usage in future
             *
             * In our production environment, only MapInfix plugin booting will cost some time
             * to be ready, it may take long time to be ready after container started
             * In this kind of situation, Zero container start up MapInfix internally first
             * to leave more time to be prepared.
             */
            MapInfix.init(vertx);
        }
    }

    /*
     * 「Vertx启动后」启动流程三
     * *：必须在Extension启动完成后执行，且作为非标准化的扩展启动部分
     * 1. 多应用管理平台
     * 2. 多租户管理平台
     * 3. 多语言管理平台
     * Enroll：登记
     */
    private static Future<Set<HArk>> startEnroll(final Vertx vertx, final HConfig config) {
        final KPivot<Vertx> pivot = KPivot.of(vertx);
        return pivot.registryAsync(config);
    }

    /*
     * 「Vertx启动后」启动流程四
     * 1. Extension 启动流程，开启Zero Extension扩展模块启动
     * 2. 只有配置了 init 节点的核心模块会被直接启动
     * Edge：边界/边缘
     */
    public static Future<Boolean> startEdge(final Vertx vertx, final HConfig config,
                                            final Set<HArk> ark) {
        if (!STORE.isInit()) {
            LOG.Env.info(ZeroArcane.class, MSG_EXT_CONFIGURATION, config);
            return Future.succeededFuture(Boolean.TRUE);
        }
        if (ZeroStore.is(YmlCore.init.__KEY)) {
            /*
             * 2. 新模式：
             * init:
             *    extension:
             *      - component: xxx
             *    compile:
             *      - component: xxx
             *        order: 1
             */
            final JsonObject initConfig = ZeroStore.option(YmlCore.init.__KEY);
            LOG.Env.info(ZeroArcane.class, MSG_EXT_COMPONENT, initConfig.encode());
            return Ux.nativeInit(initConfig, vertx);
        } else {
            // Nothing triggered when the configuration data format is invalid
            return Future.succeededFuture(Boolean.TRUE);
        }
    }
}
