package io.modello.specification.atom;

import io.modello.atom.normalize.KMarkAttribute;
import io.modello.atom.normalize.RRule;
import io.modello.eon.em.EmValue;
import io.modello.specification.meta.HMetaField;

import java.util.List;

/**
 * 模型中的属性定义，建模第一个维度，包含了复杂的数据结构用来描述属性相关信息。
 * 其中智能化解析结果如下：
 * <pre><code>
 *     1. 当 isArray = true 时，Format 默认会为 `JsonArray`，表示属性集合。
 *     2. 解析 `sourceConfig` 字段提取实际的数据格式。
 * </code></pre>
 * 属性对底层字段的限制条件如下：
 * <pre><code>
 *     1. JsonArray格式，根据属性类型的不同，会有不同的限制条件。
 *        INTERNAL：内部数据格式，配置中必须包含 fields 配置用于描述子元素，且此时的 fields 的
 *                  数据类型必须是 {@link io.vertx.core.json.JsonArray}。
 *        REFERENCE/EXTERNAL：外部数据格式，配置自由。
 *
 *     2. JsonObject格式，根据属性类型的不同，会有不同限制条件。
 *        INTERNAL：内部数据格式，配置中必须包含 fields 配置用于描述子元素，且此时的 fields 的
 *                  数据类型必须是 {@link io.vertx.core.json.JsonObject}。
 *        REFERENCE/EXTERNAL：外部数据格式，配置自由。
 *
 *     3. Elementary格式，基础数据格式
 *        这种格式中，配置数据中必须包含 `type` 属性，该属性设置成一个 Java 基础类名（反射专用），
 *        若没有配置则提取默认值：
 *        - type = {@link java.lang.String}，字符串类型的属性
 *        - source = Elementary，基础格式类型
 * 属性配置通常如下
 * {
 *     "name",
 *     "alias",
 *     "type",
 *     "format": "JsonArray, JsonObject, Elementary, Expression",
 *     "fields": [
 *         {
 *              "field": "",
 *              "alias": "",
 *              "type": "null -> String.class | ???"
 *         }
 *     ],
 *     "rule": {
 *     }
 * }
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HAttribute {
    /**
     * 返回当前属性配置中 `sourceConfig / sourceReference` 中定义的
     * 引用规则，并解析 `rule` 配置节点生成。
     *
     * @return {@link RRule}
     */
    RRule referenceRule();

    /**
     * 返回当前属性的属性标记
     *
     * @return {@link KMarkAttribute}
     */
    KMarkAttribute marker();

    /**
     * 提取当前属性的格式信息，解释一下为什么要提到格式信息，格式主要是用于逆向解析，
     * 从数据解析并构造元数据的过程，如果是正向的解析，模型中已经包含了属性定义，不论
     * 哪种实现方式，内部都已经包含属性本身的元数据信息，所以此处基于配置处理数据格式
     * 其重用性虽然有限，但对数据数据分析提取元数据十分有用，最典型的场景是导入（Excel
     * 中通常不会包含元数据信息，所以需要通过数据格式来提取元数据信息）。
     *
     * @return {@link EmValue.Format}
     */
    EmValue.Format format();
    // -------------------- 属性类型信息

    /**
     * 返回当前属性的元数据定义
     *
     * @return {@link HMetaField}
     */
    HMetaField field();

    /**
     * 返回逆向分析属性的元数据定义，此处和 compile 方法对齐，此处返回的元数据定义表示
     * 根据数据 JsonArray 分析出来的结果得到的该属性的元数据定义。和原始定义不一样在于
     * 方向不同
     * <pre><code>
     *     原始定义：现有模型定义、属性定义，然后得到属性对应的元数据
     *     逆向定义：没有任何模型定义和属性定义，直接解析 JsonArray 的数据，再结合配置
     *              中定义的属性信息，得到元数据定义。
     * </code></pre>
     * 之所以要有逆向定义在于并非所有的属性都在模型中有所定义，最典型的场景是：
     * <pre><code>
     *     1. 扩展属性
     *     2. 动态属性
     *     3. 无元数据提取场景
     *     4. 远程调用和通讯
     * </code></pre>
     *
     * @return {@link List}<{@link HMetaField}>
     */
    List<HMetaField> fieldCompiled();
}
