package io.macrocosm.specification.config;

import io.horizon.eon.em.EmApp;

/**
 * 基于启动配置存储专用配置器，内置包含
 * <pre><code>
 *     1. {@link HBoot} 本次启动详细信息
 *        - {@link HEnergy} 能量配置，系统加载的内容
 *        - {@link Class} 启动器类
 *        - {@link Class} 运行程序的主类
 *        - {@link String[]} 当前启动时的运行参数
 *     2. {@link HEnergy} 能量配置，系统加载的内容
 *        - {@link EmApp.Mode} 应用模式
 *        - {@link io.horizon.eon.em.EmBoot.LifeCycle} 生命周期
 * </code></pre>
 *
 * @author lang : 2023-05-31
 */
public interface HStation {
    /**
     * 返回核心启动配置
     *
     * @return {@link HBoot}
     */
    HBoot boot();

    /**
     * 绑定启动相关信息
     *
     * @param mainClass 主类
     * @param args      启动参数
     *
     * @return {@link HStation}
     */
    HStation bind(Class<?> mainClass, String[] args);
}
