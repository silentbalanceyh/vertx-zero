package io.modello.specification.meta;

import io.horizon.spi.modeler.MetaOn;
import io.horizon.uca.cache.Cc;
import io.modello.specification.element.HKey;
import io.vertx.core.json.JsonArray;

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


/**
 * 模型逆向分析专用接口，逆向分析接口用于处理复杂的数据格式
 * 它的核心结构如下：
 * <pre><code>
 *     1. Simple ( complex = false )
 *        (row index = 2) = 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15...
 *     2. Complex ( complex = true )
 *        (index = 3) = 0,1,2,3,4,5 = JsonArray,n,n,8,9,10 = JsonArray,n,n,n,14,15,...
 *        (index = 4) = n,n,n,n,n,5,6,7,n,n,10,11,12,13,14,15,...
 * </code></pre>
 * 其中上述格式中 index = 1 表示Excel中的 Title 部分，而 index = 1,2 也表示 Title 部分，
 * 逆向分析接口主要在于该接口中的数据结构是拉平的，和没有拉平的部分不太一致，在标准建模中通常不包含
 * 逆向分析接口，所以将逆向分析接口单独独立出来考虑。
 *
 * @author lang : 2023-05-09
 */
interface HMetaAtomMatrix {
    /**
     * 提取索引为 index 的类型信息
     *
     * @param index 索引
     *
     * @return 类型信息
     */
    Class<?> type(Integer index);

    /**
     * 提取拉平之后的所有类型信息
     *
     * @return 类型信息
     */
    List<Class<?>> type();

    /**
     * 逆向分析专用接口，根据数据分析类型
     *
     * @param matrix 数据矩阵
     *
     * @return 元数据信息
     */
    HMetaAtom compile(JsonArray matrix);
}

interface CACHE {
    Cc<String, MetaOn> CCT_META_ON = Cc.openThread();
}
