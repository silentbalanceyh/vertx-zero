package io.horizon.uca.boot;

import io.macrocosm.specification.app.HRegistry;
import io.macrocosm.specification.config.HConfig;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Future;

import java.util.Set;

/**
 * 环境注册器，替换原始的 {@see HES}
 * 当前运行环境配置，内置于 Envelop 在绑定环境时执行，主要包含两个维度：
 * <pre><code>
 *     1. {@link io.macrocosm.specification.app.HApp}
 *     2. {@link io.macrocosm.specification.secure.HoI}
 *     两个维度的核心表结构如下
 *                   HoI              HApp
 *     name           x                o
 *     code           x                o
 *     appId          x                o
 *     appKey         x                o
 *     sigma          o                o       ( mode = CUBE )
 *     tenant         o                x
 * </code></pre>
 * 真实请求过程中的输入数据只有三个核心维度：
 * <pre><code>
 *     1. tenant / language 是固定维度，通常在容器这一级就指定好了
 *     2. 上层维度：tenant
 *        下层维度：appId ( appKey )，业务部分可使用 name, code
 *     3. sigma 可以在上下层切换，主要用于标识 sigma 是租户概念还是非租户概念
 * </code></pre>
 * 根据当前启动之后注册的应用类型，可区分基础维度 sigma 是哪种
 * <pre><code>
 *     1. 「单机环境」{@link io.horizon.eon.em.EmApp.Mode#CUBE}，sigma 只有一个，HoI 可以为 null（无租户）
 *     2. 「多应用环境」{@link io.horizon.eon.em.EmApp.Mode#SPACE}，sigma = tenant 租户
 *         HoI 此时只有一个，且 children 为空
 *     3. 「多层租户环境」{@link io.horizon.eon.em.EmApp.Mode#SPACE}，sigma = tenant 租户
 *         HoI 此时只有一个，但 children 不为空
 *     4. 「多租户环境」{@link io.horizon.eon.em.EmApp.Mode#GALAXY}，sigma = 拥有者信息，而一个拥有者
 *         旗下会包含多个 tenant 租户信息，此时 HoI 不止一个，且 children 不做任何检查
 *     5. 「云租户」{@link io.horizon.eon.em.EmApp.Mode#FRONTIER} 此时跳过 sigma 无等价维度，且 HoI 会
 *         包含多个，sigma 也包含多个，而且 HoI 部分会出现自租户映射信息
 *
 *     其中上述五个维度中，容器内部只会包含「单机环境」和「多应用环境」，一旦从租户开始分离则意味着要执行不同的
 *     容器管理，每个租户拥有独立的容器，每个容器绑定一个租户和一个应用 {@link HArk}，这就是应用配置容器存在的
 *     意义，从第三维度开始，所有的实现部分都转向部署部分，而非容器部分：
 *     1) 平台应用可以创建容器和部署计划，所以平台管理端可指定租户标识
 *        1）创建 {@link io.horizon.specification.secure.HOwner} 拥有者基础账号信息
 *        2）进入平台账号对应的开发中心 {@link io.horizon.specification.under.HRAD}
 *        3）在开发中心创建项目基础信息 {@link io.horizon.specification.under.HProject}
 *        4）执行发布流程，进入用户管理端创建部署计划
 *        5）提供租户标识 tenantId / sigma 和应用标识 appId / appKey 创建容器
 *           - 底层容器 {@link io.macrocosm.specification.program.HCube}
 *           - 配置容器 {@link HArk}
 *     2) 非平台应用直接在启动时访问 {@link KPivot} 执行现阶段初始化注册阶段，将当前环境中的信息
 *        注册到运行容器中
 *        - 可使用 Z_TENANT 提供租户信息
 *        - 可使用 Z_APP 提供应用信息，多个应用时则是单租户多应用环境
 *          - 「多应用」sigma 表示租户等价概念
 *          - 「单应用」sigma 表示应用等价概念
 *        运行容器的核心配置就是 {@link HArk}，您可以直接通过代码读取当前运行环境中的信息，这种情况下 mode 的值
 *        只能是 {@link io.horizon.eon.em.EmApp.Mode#CUBE} 或 {@link io.horizon.eon.em.EmApp.Mode#SPACE}
 *        两种
 * </code></pre>
 *
 * @author lang : 2023-06-06
 */
public class KRegistry<T> implements HRegistry<T> {
    @Override
    public Set<HArk> registry(final T container, final HConfig config) {
        return Set.of(io.macrocosm.atom.context.KArk.of());
    }

    @Override
    public Future<Set<HArk>> registryAsync(final T container, final HConfig config) {
        return Future.succeededFuture(this.registry(container, config));
    }
}
