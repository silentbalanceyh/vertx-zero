package io.modello.eon.em;

/**
 * 约束分为两类：
 * <pre><code>
 *     1. 模型级约束
 *     2. 属性级约束
 * </code></pre>
 *
 * @author lang : 2023-05-08
 */
public enum RuleLevel {
    ATOM,           // 模型级约束
    ATTRIBUTE,      // 属性级约束
}
