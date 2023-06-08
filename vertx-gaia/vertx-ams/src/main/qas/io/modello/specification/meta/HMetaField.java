package io.modello.specification.meta;

import io.horizon.spi.modeler.MetaOn;
import io.horizon.util.HUt;
import io.modello.specification.element.HConstraint;
import io.modello.specification.element.HKey;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    static HMetaField of(final String name, final String alias) {
        return of(name, alias, null);
    }

    static HMetaField of(final String name, final String alias,
                         final Class<?> type) {
        final MetaOn metaOn = CACHE.CCT_META_ON.pick(
            // 缓存 + SPI 双模式，既保证性能又保证扩展
            () -> HUt.service(MetaOn.class, false),
            MetaOn.class.getName()
        );
        return Objects.isNull(metaOn) ? new MetaField(name, alias, type) : metaOn.field(name, alias, type);
    }

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
     * 返回传入字段 field 的别名
     *
     * @param field 子字段名
     *
     * @return 别名
     */
    String alias(String field);

    /**
     * 返回该字段的名称：
     *
     * @return 字段名称
     */
    String name();

    /**
     * 返回传入字段 field 的名
     *
     * @param field 子字段名
     *
     * @return 字段名称
     */
    String name(String field);

    /**
     * 返回该字段的类型
     *
     * @return 字段类型
     */
    Class<?> type();

    /**
     * 返回传入字段 field 的类型
     *
     * @param field 子字段名
     *
     * @return 字段类型
     */
    Class<?> type(String field);
}


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
