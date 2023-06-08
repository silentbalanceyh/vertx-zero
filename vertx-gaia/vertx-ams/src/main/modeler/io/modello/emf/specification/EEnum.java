package io.modello.emf.specification;

import io.modello.specification.element.HOption;

import java.util.List;

/**
 * @author lang : 2023-05-13
 */
public interface EEnum<T> extends EClass<T> {
    /**
     * 返回枚举值列表
     * <pre><code>
     *     1. key: 主键
     *     2. name：名称
     *     3. value：值
     *     4. label -> literal：字面量值
     *
     *     对应片段：<eLiterals name="UNSPECIFIED" value="1" literal="unspecified"/>
     * </code></pre>
     *
     * @return 枚举值列表
     */
    List<HOption> options();

    /**
     * 枚举类型专用，返回单个枚举值
     *
     * @param name 枚举值名称
     *
     * @return 枚举值
     */
    HOption option(String name);

    /**
     * 接口中已经包含了 type() 方法返回Java类型，此处方法重载于 type() 方法，用于返回
     * 单个枚举值对应的 {@link Enum} 类型。
     *
     * @param <V> 枚举类型
     *
     * @return 枚举类型
     */
    <V extends Enum<V>> Class<V> value(String name);
}
