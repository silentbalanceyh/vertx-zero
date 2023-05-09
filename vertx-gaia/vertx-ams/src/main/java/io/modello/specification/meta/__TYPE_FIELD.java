package io.modello.specification.meta;

import io.modello.specification.element.HKey;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 复杂类型扩展专用，当一个属性是复杂属性时会采用这种方式进行属性搭建
 * 此接口专用于描述复杂类型的子属性集。
 * 复杂属性部分包含如下：
 * <pre><code>
 *     1. 带顺序的列表集合：[]
 *     2. 不带顺序的集合：{}
 * </code></pre>
 *
 * @author lang : 2023-05-08
 */
interface HMetaFieldChildren {
    /**
     * 添加子结构中的元数据信息
     *
     * @param children 子结构中的元数据信息
     *
     * @return 当前引用
     */
    HMetaField add(Collection<HMetaField> children);

    /**
     * 添加一个子结构中的元数据信息
     *
     * @param name  子结构中的属性名
     * @param child 子结构元模型定义
     *
     * @return 当前引用
     */
    HMetaField add(String name, HMetaField child);

    /**
     * 更新一个子结构中的元数据信息
     *
     * @param name  子结构中的属性名
     * @param child 子结构元模型定义
     *
     * @return 当前引用
     */
    HMetaField set(String name, HMetaField child);

    /**
     * 返回子结构中的所有元模型定义信息
     *
     * @return 子结构中的所有元模型定义信息
     */
    List<HMetaField> children();

    /**
     * 判断当前字段是否复杂字段，复杂字段定义
     * <pre><code>
     *    1. JsonArray / JsonObject
     *    2. List / Set
     *    3. Map
     * </code></pre>
     *
     * @return 是否复杂字段
     */
    default boolean isComplex() {
        return false;
    }
}

/**
 * 复杂类型扩展专用，当一个属性是复杂属性时会采用这种方式进行属性搭建
 * 此接口主要用于描述复杂类型的子类型对应的键信息，键信息默认采用唯一
 * 键的方式处理模型中的键信息。
 *
 * @author lang
 */
interface HMetaFieldKey {
    /**
     * 设置复杂类型键相关信息
     *
     * @param keys 复杂类型键
     *
     * @return 复杂类型键
     */
    HMetaField key(Set<String> keys);

    /**
     * 返回复杂类型键相关信息，通常此键由多个字段构成
     *
     * @param key 复杂类型键
     *
     * @return 复杂类型键
     */
    HMetaField key(HKey key);

    /**
     * 返回复杂类型拥有的键信息
     *
     * @return 复杂类型键
     */
    HKey key();
}
