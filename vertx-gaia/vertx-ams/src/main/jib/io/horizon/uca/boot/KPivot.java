package io.horizon.uca.boot;

import io.horizon.fn.HFn;
import io.horizon.util.HUt;
import io.macrocosm.atom.context.KAmbient;
import io.macrocosm.specification.app.HAmbient;
import io.macrocosm.specification.app.HRegistry;
import io.macrocosm.specification.config.HConfig;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Future;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 上下文环境桥
 * <pre><code>
 *     1. 针对 {@link HRegistry} 的桥接封装
 *     2. 从 SPI 中提取 HRegistry，若无法找到则执行默认流程（Min）方式处理
 * </code></pre>
 *
 * @author lang : 2023-06-06
 */
@SuppressWarnings("all")
public class KPivot<T> {
    /**
     * {@link HAmbient} 中存储了当前运行的上下文环境，该上下文环境中会包含对应的核心运行信息
     * 此信息可以替换原始架构中的主架构
     * <pre><code>
     *     废弃的部分
     *     1. Core,     KApp / KTenant
     *     2. zero-jet, Ambient / AmbientEnvironment / JtApp
     *     替换结构
     *     1. {@link HAmbient}
     *        key = {@link io.macrocosm.specification.program.HArk}
     *               - {@link io.macrocosm.specification.app.HApp}
     *               - {@link io.modello.atom.app.KDS}
     *                  - {@link io.modello.atom.app.KDatabase}
     *               - {@link io.horizon.specification.app.HBelong}
     *                  - {@link io.macrocosm.specification.secure.HFrontier}
     *                  - {@link io.macrocosm.specification.secure.HGalaxy}
     *                  - {@link io.macrocosm.specification.secure.HSpace}
     *     2. {@link HRegistry}
     *        当前运行的 {@link HAmbient} 为一个单独的实例，也运行在单节点中
     * </code></pre>
     */
    private static final HAmbient RUNNING = KAmbient.of();
    private final T container;
    private final HRegistry<T> context;

    private final HRegistry<T> extension;

    private KPivot(final T container) {
        this.container = container;
        this.context = new KRegistry<>();
        this.extension = HUt.service(HRegistry.class, false);
    }

    public static HAmbient running() {
        synchronized (KPivot.class) {
            return RUNNING;
        }
    }

    public static <T> KPivot<T> of(final T container) {
        return new KPivot<>(container);
    }

    public Set<HArk> registry(final HConfig config) {
        // 前置检查（注册拦截）
        KPivotKit.fail(getClass(), RUNNING);

        Set<HArk> contextDefault = this.context.registry(this.container, config);
        final Set<HArk> contextCombine = new HashSet<>();
        if (Objects.nonNull(this.extension)) {
            final Set<HArk> contextExtension = this.extension.registry(this.container, config);
            contextCombine.addAll(KPivotKit.combine(contextDefault, contextExtension));
        }
        contextCombine.forEach(RUNNING::registry);
        return contextCombine;
    }

    public Future<Set<HArk>> registryAsync(final HConfig config) {
        // 前置检查（异步注册拦截）
        return KPivotKit.failAsync(getClass(), RUNNING).compose(nil ->
            HFn.<Set<HArk>, Set<HArk>, Set<HArk>>combineT(
                // 第一个异步结果
                () -> this.context.registryAsync(this.container, config),
                // 第二个异步结果
                () -> this.registryExtension(config),
                // 合并函数
                this::registryOut
            ));
    }

    // ------------------------ 私有部分 -----------------------
    private Future<Set<HArk>> registryOut(final Set<HArk> source, final Set<HArk> extension) {
        final Set<HArk> combine = KPivotKit.combine(source, extension);
        combine.forEach(RUNNING::registry);
        return Future.succeededFuture(combine);
    }

    private Future<Set<HArk>> registryExtension(final HConfig config) {
        if (Objects.isNull(this.extension)) {
            return Future.succeededFuture();
        } else {
            return this.extension.registryAsync(this.container, config);
        }
    }
}
