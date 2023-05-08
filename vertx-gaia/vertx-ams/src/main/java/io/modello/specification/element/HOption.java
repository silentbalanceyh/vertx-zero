package io.modello.specification.element;

import io.modello.annotations.EA;
import io.modello.eon.VEmf;
import io.modello.specification.ui.HJsx;

/**
 * 项结构，在前端和后端都可以使用，主要包含四个核心属性
 * <pre><code>
 * - key         元素主键，该属性不用于 EMF 建模，但可以用于前端的数据绑定
 * - name        「EMF」元素名称
 * - value       「EMF」元素值
 * - label       「EMF」元素标签
 * </code></pre>
 *
 * 此处 Literal 主要针对 EMF 中的 Literal 类型，如：
 * <pre><code>
 * 后端：EMF
 * <eLiterals name="NONE" value="1" literal="none"/>
 * 前端：Jsx
 * React -> Option
 * [
 *     key,
 *     value,
 *     label ( literal )
 * ]
 * </code></pre>
 * 此标签通常在枚举定义中实现。
 *
 * @author lang : 2023-05-08
 */
@EA.Node(VEmf.N_LITERALS)
public interface HOption extends HJsx {
    /**
     * eLiterals 中的 name 属性
     *
     * @return name
     */
    @EA.Attribute(VEmf.V_NAME)
    String name();

    /**
     * eLiterals 中的 value 属性
     *
     * @return value
     */
    @EA.Attribute(VEmf.V_VALUE)
    String value();

    /**
     * eLiterals 中的 literal 属性
     *
     * @return literal
     */
    @EA.Attribute(VEmf.V_LITERAL)
    String literal();
}
