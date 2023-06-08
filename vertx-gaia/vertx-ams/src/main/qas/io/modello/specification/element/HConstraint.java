package io.modello.specification.element;

import io.horizon.specification.executor.HValidator;

import java.util.Set;

/**
 * （属性集）针对规则的定义，不同的规则会限定底层不同类型的约束，约束本身定义使用枚举
 *
 * @author lang : 2023-05-08
 */
public interface HConstraint {
    /**
     * 当前属性值列表约束
     *
     * @return {@link Set}
     */
    Set<String> values();

    /**
     * 验证器扩展，针对属性专用
     *
     * @return {@link HValidator}
     */
    HValidator validator();
}
