package io.modello.specification.element;

import java.util.List;

/**
 * 「成员」
 * 为了保证后期的扩展处理，所以此处直接从高阶直接对接到语言级的核心处理，该类用于描述属性相关信息
 * <pre><code>
 *     1. {@link HField} 属性清单
 * </code></pre>
 *
 * @author lang : 2023-05-22
 */
public interface HFeature extends HModifier, HType {
    /**
     * 属性引用信息
     *
     * @return {@link HClassifier}
     */
    HClassifier reference();

    /**
     * 约束基础定义
     *
     * @return {@link List}
     */
    List<HConstraint> constraint();
}
