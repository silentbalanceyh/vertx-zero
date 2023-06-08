package io.macrocosm.specification.config;

import io.horizon.eon.em.EmApp;
import io.horizon.eon.em.EmBoot;

/**
 * @author lang : 2023-05-31
 */
public interface HEnergy {
    /**
     * 绑定生命周期组件
     *
     * @param lifeCycle 生命周期
     * @param clazz     类名
     *
     * @return 自身
     */
    HEnergy bind(EmBoot.LifeCycle lifeCycle, Class<?> clazz);

    /**
     * 配置绑定
     *
     * @param clazz     配置组件类型
     * @param reference 配置对象
     *
     * @return 自身
     */
    HEnergy bind(Class<?> clazz, HConfig reference);

    /**
     * 绑定云端应用组件
     *
     * @param appMode 应用模式
     * @param clazz   组件类型
     *
     * @return 自身
     */
    HEnergy bind(EmApp.Mode appMode, Class<?> clazz);

    /**
     * 绑定研发中心组件类名
     *
     * @param rad 研发中心组件类名
     *
     * @return 自身
     */
    HEnergy rad(Class<?> rad);

    /**
     * @return 研发中心组件类名
     */
    Class<?> rad();

    /**
     * 绑定应用类型
     *
     * @param clazz 应用类型
     *
     * @return 自身
     */
    HConfig config(Class<?> clazz);

    /**
     * @param mode 应用模式
     *
     * @return 应用组件
     */
    Class<?> component(EmApp.Mode mode);

    /**
     * @param lifeCycle 生命周期
     *
     * @return 应用组件
     */
    Class<?> component(EmBoot.LifeCycle lifeCycle);
}
