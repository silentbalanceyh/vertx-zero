package io.macrocosm.specification.nc;

import io.vertx.core.Vertx;

/**
 * @author lang : 2023-05-20
 */
public interface HWrapper {
    /**
     * 启动器泛型构造，构造想要启动的组件实例
     *
     * @param interfaceCls 核心组件类
     * @param vertx        Vertx实例
     * @param <C>          组件类型
     *
     * @return 组件实例
     */
    <C> C starter(Class<?> interfaceCls, Vertx vertx);

    /**
     * 启动器泛型构造，构造想要启动的组件实例
     *
     * @param interfaceCls 核心组件类
     * @param vertx        Vertx实例
     * @param defaultCls   默认类
     * @param <C>          组件类型
     *
     * @return 组件实例
     */
    <C> C starter(Class<?> interfaceCls, Vertx vertx, Class<?> defaultCls);
}
