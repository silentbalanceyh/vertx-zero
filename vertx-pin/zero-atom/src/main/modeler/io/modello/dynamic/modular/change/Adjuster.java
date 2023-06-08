package io.modello.dynamic.modular.change;

import java.util.Objects;

/*
 * 待确认变更新接口，用于生成待确认
 * 需要连接兼容性规则
 * 主要用于将 CMDB 的规则和平台同步
 */
public interface Adjuster {
    /*
     * 根据定义类型读取 Adjust 的信息
     */
    static Adjuster get(final Class<?> clazz) {
        return Objects.isNull(clazz) ? null : Pool.POOL_ADJUST.getOrDefault(clazz, null);
    }

    /*
     * 定义一些特殊的数据转换规则
     * 绑定配置文件：runtime/
     */
    Object inValue(Object ucmdbInput);

    /*
     * 定义一些特殊的数据转换规则
     * 绑定配置文件：runtime/
     */
    Object outValue(Object input);

    /*
     * 待确认变更专用比较
     */
    boolean isSame(Object oldValue, Object newValue);
}
