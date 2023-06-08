package io.modello.specification.meta;

import io.horizon.spi.modeler.MetaOn;
import io.horizon.util.HUt;
import io.vertx.core.json.JsonArray;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * 模型元数据专用接口，作为建模的基础结构，用来描述任意模型下的类型树相关信息
 * 有几点规则需在此处说明：
 * <pre><code>
 *     1. 关于模型的 complex 计算：只要该模型中的某个字段出现了 isComplex = true
 *        的情况，则该模型的 complex = true
 *     2. 添加有四个核心重载方法，用于追加单属性相关信息
 * </code></pre>
 *
 * 元模型作为模型定义的基础，用来针对模型本身执行分析和结构化，辅助序列化手段
 * <pre><code>
 *     1. 可以快速从 Json 结构反序列化到模型定义（定义描述内容）
 *     2. 可以快速从模型定义到 Json 结构（生成模型描述）
 * </code></pre>
 *
 * @author lang : 2023-05-09
 */
public interface HMetaAtom extends HMetaAtomMatrix {

    static HMetaAtom of() {
        final MetaOn metaOn = CACHE.CCT_META_ON.pick(
            // 缓存 + SPI 双模式，既保证性能又保证扩展
            () -> HUt.service(MetaOn.class, false),
            MetaOn.class.getName()
        );
        return Objects.isNull(metaOn) ? new MetaAtom() : metaOn.atom();
    }

    /**
     * 添加单个字段到模型中
     *
     * @param field 字段
     *
     * @return 模型
     */
    HMetaAtom add(HMetaField field);

    /**
     * 添加单个字段到模型中
     *
     * @param name  字段名
     * @param alias 字段别名
     * @param type  字段类型
     *
     * @return 模型
     */
    HMetaAtom add(String name, String alias, Class<?> type);

    /**
     * 别名为 null 的清空
     *
     * @param name 字段名
     * @param type 字段类型
     *
     * @return 模型
     */
    default HMetaAtom add(final String name, final Class<?> type) {
        return this.add(name, null, type);
    }

    /**
     * 添加单个字段到模型中：自动切换成 complex = true
     *
     * @param name     字段名
     * @param alias    字段别名
     * @param children 字段子集
     *
     * @return 模型
     */
    HMetaAtom add(String name, String alias, Collection<HMetaField> children);

    /**
     * 别名为空的情况
     *
     * @param name     字段名
     * @param children 字段子集
     *
     * @return 模型
     */
    default HMetaAtom add(final String name, final Collection<HMetaField> children) {
        return this.add(name, null, children);
    }

    /**
     * 清空当前模型的所有原始定义
     *
     * @return 模型
     */
    HMetaAtom clear();
    // -------------------- 数据提取类：双层提取 -----------------------

    /**
     * 判断当前模型是否复杂模型
     *
     * @return 是否复杂模型
     */
    boolean isComplex();

    /**
     * 判断某个字段是否复杂字段
     *
     * @param field 字段名
     *
     * @return 是否复杂字段
     */
    boolean isComplex(String field);

    /**
     * 返回当前模型中的字段数量
     *
     * @return 字段数量
     */
    int size();

    /**
     * 返回某个字段的子字段数量
     *
     * @param field 字段名
     *
     * @return 子字段数量
     */
    int size(String field);

    /**
     * 返回某个字段的字段类型
     *
     * @param field 字段名
     *
     * @return 字段类型
     */
    Class<?> type(String field);

    /**
     * 返回某个字段中的子字段类型
     *
     * @param field      字段名
     * @param childField 子字段名
     *
     * @return 子字段类型
     */
    Class<?> type(String field, String childField);

    // -------------------- 数据提取类：普通提取 -----------------------

    /**
     * 别名提取：field = alias，生成前端常用绑定信息
     *
     * @return 别名提取
     */
    ConcurrentMap<String, String> alias();

    /**
     * 读取传入字段 field 的字段原始定义
     *
     * @param field 字段名
     *
     * @return 字段原始定义
     */
    HMetaField field(String field);
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
