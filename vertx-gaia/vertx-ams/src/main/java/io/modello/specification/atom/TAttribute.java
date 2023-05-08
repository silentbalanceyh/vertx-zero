package io.modello.specification.atom;

/**
 * 字段类型专用接口，作为建模最元素级的基础结构，可用来描述任何类型的字段。
 * 所有情况如下：
 * <pre><code>
 * 1. 纯字段相关信息（内部 type 必须是非集合型结构）     isComplex = false
 * 2. 子字段包含（内部 type 必须是集合型结构）          isComplex = true
 *    -- childList（JsonArray.class / List.class）
 *    -- childMap（JsonObject.class / Map.class）
 * 3. 桥接约束接口 {@link io.modello.specification.rule.HConstraint}，当前属性中所有的约束信息
 * </code></pre>
 *
 * @author lang : 2023-05-08
 */
public interface TAttribute extends TAttributeChildren {

}
