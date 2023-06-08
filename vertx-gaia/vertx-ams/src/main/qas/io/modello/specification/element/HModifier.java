package io.modello.specification.element;

import io.modello.eon.em.ScopeType;

/**
 * 「修饰符」Mark
 * <hr/>
 * 语言级的属性相关信息
 * <pre><code>
 *    1. 访问控制修饰符
 * </code></pre>
 *
 * @author lang : 2023-05-22
 */
public interface HModifier {
    /**
     * 访问控制修饰符
     *
     * @return {@link ScopeType}
     */
    ScopeType scope();

    /**
     * 修饰符专用
     *
     * @return {@link HMark}
     */
    HMark mark();
}
