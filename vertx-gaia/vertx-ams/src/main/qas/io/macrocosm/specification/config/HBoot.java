package io.macrocosm.specification.config;

import io.horizon.eon.em.EmApp;

/**
 * @author lang : 2023-05-31
 */
public interface HBoot {
    /**
     * {@link EmApp.Type} 返回应用类型，现阶段有三种
     * <pre><code>
     *     1. APPLICATION: 单独应用程序
     *     2. SERVICE：微服务组件后端
     *     3. GATEWAY：微服务组件网关
     * </code></pre>
     *
     * @return {@link EmApp.Type}
     */
    EmApp.Type app();

    /**
     * 绑定应用类型
     *
     * @param type {@link EmApp.Type}
     *
     * @return {@link HBoot}
     */
    HBoot app(EmApp.Type type);

    /**
     * 绑定主类和参数表
     * <pre><code>
     *     - mainClass -> target
     *     - arguments -> args
     * </code></pre>
     *
     * @param mainClass 启动主类
     * @param arguments 参数
     *
     * @return {@link HBoot}
     */
    HBoot bind(Class<?> mainClass, String... arguments);

    /**
     * 本次启动的参数表
     *
     * @return {@link String[]}
     */
    String[] args();

    /**
     * 本次启动主类
     *
     * @return {@link Class}
     */
    Class<?> target();

    /**
     * 从系统中提取的启动器类
     *
     * @return {@link Class}
     */
    Class<?> launcher();

    /**
     * 本次启动的能量配置
     *
     * @return {@link HEnergy}
     */
    HEnergy energy();
}
