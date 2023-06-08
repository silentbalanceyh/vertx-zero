package io.modello.specification.atom;

import io.horizon.annotations.reference.One2One;
import io.modello.specification.element.HClassifier;
import io.modello.specification.element.HField;
import io.modello.specification.element.HKey;

import java.util.Set;

/**
 * 「核心实体模型」Entity
 * <hr/>
 * 实体模型 Entity 和 Classifier 形成 1:1 的绑定，目的有二：
 * <pre><code>
 *     1. 通过 Entity 可以获取到 Classifier 的所有属性信息
 *     2. Classifier 是参考 EMF 形成的语言级模型定义
 * </code></pre>
 * 该接口继承了 {@link HLife} 实体由于属于数据结构
 *
 * @author lang : 2023-05-22
 */
public interface HEntity extends HLife {
    /**
     * 主键定义
     *
     * @return {@link HKey}
     */
    HKey key();

    /**
     * 当前模型的成员集，开启遍历专用
     *
     * @return {@link Set}
     */
    Set<String> field();

    /**
     * 语言级模型的属性信息，根据属性名称提取
     *
     * @param name 属性名
     *
     * @return {@link HField}
     */
    HField field(String name);

    /**
     * 「OO」类
     * 内部类型定义，语言级的类型定义，用于处理关联语言级部分的扩展
     *
     * @return {@link HClassifier}
     */
    @One2One
    HClassifier type();
}
