package io.horizon.eon.em;

/**
 * @author lang : 2023-05-31
 */
public final class EmApp {
    private EmApp() {
    }

    /**
     * 应用专用维度
     * 原生云模式下，开始有「云租户」和「空间租户」的概念，开容器的条件
     * 1. 维度一：语言容器：             X-Lang / language        （不同语言配置空间不同）
     * 2. 维度二：租户容器：             X-Tenant / tenant        （不同租户配置空间不同）
     *
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    public enum Mode {
        /*
         * 「独立模式」
         * 等价维度（现在使用的模式）
         * - sigma
         * - appId
         * 整个Zero运行成一个独立应用，这个应用拥有唯一的应用ID，且这个应用的 sigma 也只有
         * 唯一的一个，sigma 的值也可标识应用，不开容器也可部署。
         *
         * 1 sigma -- x tenant -- 1 appId
         */
        CUBE,

        /*
         * 「多租户模式」（自动多应用）
         * 等价维度
         * - sigma
         * - tenant
         * 此时，不同的租户会开不同的空间，Zero运行成一个平台应用，应用ID独立开容器，一个租户
         * 会包含一个或多个 app
         *
         * 1 sigma -- 1 tenant -- n appId
         */
        SPACE,

        /*
         * 「多层租户模式」（自动多应用）
         * 等价维度（无）
         *
         * 梯度维度
         * - 维度一：sigma，代表统一标识符（云租户）
         * - 维度二：tenant，代表二层租户（空间租户）
         * - 维度三：appId，一个租户会包含一个或多个 app
         *
         * 1 sigma -- n tenant -- n appId
         */
        GALAXY,

        FRONTIER,
    }

    /**
     * 「容器对接协议」
     * <hr/>
     * 每种协议中只能包含一种组件，对于一个完整的 HLife 对接流程，对接协议必须全部满足，否则无法完成对接。
     * <pre><code>
     *     1. 应用所属运行端 {@link io.macrocosm.specification.program.HNova}
     *     2. 应用所属CRI {@link io.macrocosm.specification.program.HCRI}
     *     3. 应用租户模式
     *        {@link io.macrocosm.specification.secure.HFrontier}
     *        {@link io.macrocosm.specification.secure.HGalaxy}
     *        {@link io.macrocosm.specification.secure.HSpace}
     *        此模式会直接调用内部的 `realm()` 方法，返回对应的租户边界
     *     4. 关联部署计划 {@link io.macrocosm.specification.boot.HDeployment}
     *        - 目标容器 {@link io.macrocosm.specification.program.HArk}
     *        - 源容器 {@link io.macrocosm.specification.app.HBackend}
     * </code></pre>
     *
     * @author lang : 2023-05-21
     */
    public enum Online {
        ZONE,                       // HNova
        CONTAINER,                  // HCri
        DEPLOYMENT,                 // HDeployment
        DEPLOYMENT_TARGET,          // HArk
        DEPLOYMENT_SOURCE,          // HBackend
    }

    /**
     * @author lang : 2023-05-30
     */
    public enum Type {
        APPLICATION,       // 单机应用
        GATEWAY,           // 微服务下的 Api Gateway
        SERVICE,           // 云端服务应用
    }
}
