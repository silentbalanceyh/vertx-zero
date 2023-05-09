package io.modello.specification.element;

import io.horizon.eon.VString;
import io.modello.specification.meta.HMetaField;

import java.io.Serializable;
import java.util.Objects;

/**
 * 可以跨 EMF 专用的类型接口设计，作为核心底座替换原始的 {@link HMetaField} 实现做
 * 接口规范设计，同时可以跨框架做依赖对接，如：
 * <pre><code>
 *     1. Spring Framework 中可用于解析模型文件生成对应的模型
 *     2. Vert.x 中可用于解析Json文件生成对应的模型
 *     3. Zero 中的 zero-atom 可用于解析 Excel 文件生成对应的模型
 * </code></pre>
 *
 * 字段类型的属性定义：
 * <pre><code>
 *     1. type: Java类型
 *     2. typeName：Java类型字符串模式
 * </code></pre>
 *
 * @author lang : 2023/5/5
 */
public interface HType extends Serializable {
    /**
     * Java 类型返回
     *
     * @return Java 类型
     */
    Class<?> type();

    /**
     * Java 类型的字符串模式
     *
     * @return Java 类型的字符串模式
     */
    default String typeName() {
        return Objects.isNull(this.type()) ? null : this.type().getName();
    }

    /**
     * EMF 类型名称返回
     *
     * @return EMF 类型
     */
    default String eTypeName() {
        return VString.EMPTY;
    }
}
