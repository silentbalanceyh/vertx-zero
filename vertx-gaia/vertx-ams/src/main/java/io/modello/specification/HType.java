package io.modello.specification;

import java.io.Serializable;

/**
 * 可以跨 EMF 专用的类型接口设计，作为核心底座替换原始的 {@link io.modello.atom.typed.MetaField} 实现做
 * 接口规范设计，同时可以跨框架做依赖对接，如：
 * <pre><code>
 *     1. Spring Framework 中可用于解析模型文件生成对应的模型
 *     2. Vert.x 中可用于解析Json文件生成对应的模型
 *     3. Zero 中的 zero-atom 可用于解析 Excel 文件生成对应的模型
 * </code></pre>
 *
 * 字段属性维度包括：
 *
 * <pre><code>
 * 1.
 * </code></pre>
 *
 * @author lang : 2023/5/5
 */
public interface HType extends Serializable {

}
