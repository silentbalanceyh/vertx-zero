package io.modello.specification.meta;

import io.modello.specification.element.HConstraint;

/**
 * 字段类型专用接口，作为建模最元素级的基础结构，可用来描述任何类型的字段。
 * 所有情况如下：
 * <pre><code>
 * 1. 纯字段相关信息（内部 type 必须是非集合型结构）     isComplex = false
 * 2. 子字段包含（内部 type 必须是集合型结构）          isComplex = true
 *    -- childList（JsonArray.class / List.class）
 *    -- childMap（JsonObject.class / Map.class）
 * 3. 桥接约束接口 {@link HConstraint} 当前属性中所有的约束信息
 * 4. 子字段中包含 {@link io.modello.specification.element.HKey} 做主键描述
 * </code></pre>
 *
 * @author lang : 2023-05-08
 */
public interface HMetaField extends HMetaFieldChildren, HMetaFieldKey {
    /**
     * 返回该字段的别名，别名用于设置：
     * <pre><code>
     *     1. 设置 label 标签
     *     2. 设置 display 显示值（暂定）
     *     3. 设置 literal 字面量（暂定）
     * </code></pre>
     *
     * @return 别名
     */
    String alias();

    /**
     * 设置该字段的别名
     *
     * @param alias 别名
     *
     * @return 别名
     */
    HMetaField alias(String alias);

    /**
     * 返回该字段的名称：
     *
     * @return 字段名称
     */
    String name();

    /**
     * 设置该字段的名称
     *
     * @param name 字段名称
     *
     * @return 字段名称
     */
    HMetaField name(String name);

    /**
     * 返回该字段的类型
     *
     * @return 字段类型
     */
    Class<?> type();

    /**
     * 设置该字段的类型
     *
     * @param type 字段类型
     *
     * @return 字段类型
     */
    HMetaField type(Class<?> type);
}
