package io.modello.specification.rule;

import io.modello.annotations.EA;

import java.util.concurrent.ConcurrentMap;

/**
 * （属性集）针对规则的定义，不同的规则会限定底层不同类型的约束，约束本身定义使用枚举
 *
 * @author lang : 2023-05-08
 */
@EA.Segment
public interface HConstraint {

    ConcurrentMap<String, String> eAttributes();
}
