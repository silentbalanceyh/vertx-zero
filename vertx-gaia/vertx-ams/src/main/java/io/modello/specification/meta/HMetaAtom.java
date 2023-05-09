package io.modello.specification.meta;

import io.horizon.spi.modeler.MetaOn;
import io.horizon.util.HUt;

import java.util.Collection;
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
