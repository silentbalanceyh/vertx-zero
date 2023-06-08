package io.macrocosm.specification.app;

import io.horizon.eon.em.EmApp;
import io.horizon.util.HUt;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 * 应用容器入口，针对不同的应用提供不同的 {@link io.macrocosm.specification.program.HArk} 结构
 * <pre><code>
 *     1. 多租户 / 多应用    N : N
 *     2. 单租户 / 多应用    1 : N
 *     3. 单租户 / 单应用    1 : 1
 * </code></pre>
 * {@link HRegistry} 初始化时填充当前对象，根据智能化结果执行最终内容
 * </p>
 *
 * @author lang : 2023-06-05
 */
public interface HAmbient {
    /**
     * 应用模式
     *
     * @return {@link EmApp.Mode}
     */
    EmApp.Mode mode();

    /**
     * 不论单多，获取当前运行的所有信息
     * <pre><code>
     *     1. 从数据库 X_APP 中加载读取应用程序信息，这种模式应用只有一个
     *        环境变量辅助提供应用名称，使用此名称去 X_APP 中查找唯一应用
     *     2. 直接从环境变量中提取租户ID信息，构造 KTenantOld，后续所有操作都可以依靠它完成
     * </code></pre>
     * 当前运行的所有信息会牵涉到容器的概念，在正式部署环境中：
     * <pre><code>
     *     - 若是单机运行部署，那么在 {@link io.macrocosm.specification.boot.HDeployment} 计划中可
     *       直接提取 ( APP, TENANT ) 信息。
     *     - 若是平台运行部署，则可以返回所有信息。
     * </code></pre>
     *
     * @return {@link io.macrocosm.specification.program.HArk}
     */
    HArk running(String key);

    HArk running();

    /**
     * 返回所有的应用容器信息，此处的 key 基于如下计算：
     * <pre><code>
     *     1. 第一部分为当前 {@link HArk} 对应的 {@link io.horizon.specification.app.HBelong} 中的
     *        owner()，此值全局唯一
     *     2. 第二部分基于 {@link HArk} 中的 {@link HApp} 计算
     *        - name(), 可来自于存储
     *        - ns(), 可来自容器或存储
     * </code></pre>
     *
     * @return {@link java.util.concurrent.ConcurrentMap}
     */
    ConcurrentMap<String, HArk> app();

    /**
     * 扩展模块配置
     *
     * @param name      模块名称
     * @param configCls 模块配置类
     * @param <T>       模块配置泛型
     *
     * @return 模块配置
     */
    default <T> T extension(final String name, final Class<T> configCls) {
        return HUt.deserialize(this.extension(name), configCls);
    }

    JsonObject extension(final String name);

    /**
     * 注册应用容器信息，注册容器的基本流程
     * <pre><code>
     *     1. 应用容器中的 {@link io.horizon.specification.app.HBelong} 会隶属不同层级的
     *        基本信息，层级判断基于它的子类
     *     2. 内置的应用程序池只有在注册之后尺寸 > 1 时执行抽象层判断，否则注册就是单纯的类型
     *     3. 如果程序池中只有一个 {@link HArk}，则 mode = {@link EmApp.Mode#CUBE}，默认值
     *        若程序池中出现了超过 1 个应用，此处的 mode 值依赖 {@link io.horizon.specification.app.HBelong}
     *        的实际类型
     *        - {@link io.macrocosm.specification.secure.HFrontier}, mode = {@link EmApp.Mode#FRONTIER}
     *        - {@link io.macrocosm.specification.secure.HGalaxy}, mode = {@link EmApp.Mode#GALAXY}
     *        - {@link io.macrocosm.specification.secure.HSpace}, mode = {@link EmApp.Mode#SPACE}
     * </code></pre>
     *
     * @param ark 应用容器
     *
     * @return {@link io.macrocosm.specification.app.HAmbient}
     */
    HAmbient registry(HArk ark);

    HAmbient registry(String extension, JsonObject configuration);
}
