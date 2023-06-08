package io.vertx.up.lightway;

import io.macrocosm.specification.boot.HOn;
import io.vertx.up.runtime.ZeroAnno;

import java.util.Objects;

/**
 * 配置初始化流程
 * <pre><code>
 *     1. 环境初始化，执行 {@link ZeroAnno#configure()} 方法
 *     2. 填充功能表
 *        - shared
 *        - session
 *        - etcd
 *        - gateway
 *     3. 计算 BootStore 基础配置
 * </code></pre>
 *
 * @author lang : 2023-05-30
 */
public class OnBoot implements HOn<OnBootConfig> {

    private static OnBootConfig INSTANCE;

    public static OnBootConfig configuration() {
        Objects.requireNonNull(INSTANCE);
        return INSTANCE;
    }

    /**
     * 完整步骤和流程：
     * <pre><code>
     *     1. 开启包扫描仪（静态）
     *        - 在 Vertx 实例启动之前，必须扫描所有符合Zero标准的元数据 Annotation
     *        - 针对核心注解进行对象构建，在底层开启并行模式，新扫描算法
     *        - 针对 Extension 部分，通过依赖扫描的方式处理启动组件部分
     *     2. 扫描完成后，构造启动配置
     *        - 基础启动配置构造（启动配置中的应用标记了应用模式，内置调用不同的启动器）
     *        - 功能表 {@link io.vertx.up.eon.em.FeatureMark}
     *     3. 功能表会在后续执行过程中传入到 {@link io.vertx.up.boot.anima.Scatter} 中重
     *        新计算，所以要重写 Scatter 连接专用方法
     * </code></pre>
     *
     * @param config 输入参数，{@link io.macrocosm.specification.config.HConfig} 实例
     *
     * @return {@link Boolean} 实例
     */
    @Override
    public Boolean configure(final OnBootConfig config) {
        // 1. 环境初始化
        ZeroAnno.configure();
        // 2. 扫描环境之后才可以拿到 Injections
        INSTANCE = config;
        // 3. 装配完成
        return Boolean.TRUE;
    }

    @Override
    public OnBootConfig store() {
        return configuration();
    }
}
