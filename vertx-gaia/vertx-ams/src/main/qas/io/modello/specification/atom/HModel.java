package io.modello.specification.atom;

import io.macrocosm.specification.program.HArk;
import io.modello.atom.normalize.KMarkAtom;
import io.modello.eon.em.EmModel;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 「核心模型」Model
 * <hr/>
 * 系统中的完整模型包含三层：
 * <pre><code>
 *     1. 顶层模型，类型为：{@link HAtom}，以容器为主体，封装 {@link HModel} 的业务外衣，形成高阶容器模型。
 *     2. 核心模型，类型为 {@link HModel}，一级抽象模型，包含了非语言级的上层模型。
 *     3. 语言模型，类型为 {@link HEntity}，二级抽象模型，包含了语言级的下层模型。
 *     4. 整体模型结构如下：
 *        - HAtom
 *          |-- HModel                      HReference / HAttribute         （容器级定义）
 *              |-- HEntity                 HField                          （业务级定义）
 *                  |-- HClassifier         HFeature                        （XML语言级定义）
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HModel extends HLife {

    /**
     * 规则级，规则在整体建模中分两级
     * <pre><code>
     *     1. 模型级，直接存储在元模型数据中的模型级的规则：{@link HRule}，该规则通常是存储在数据库中或
     *        文件中，它直接和 Unique 关联到一起，形成模型的唯一标识。
     *     2. 操作级，这种规则通常和操作组件直接绑定，且直接在运行时计算且单独绑定到组件上，形成具有业务
     *        独立性的实时绑定
     * </code></pre>
     *
     * @return {@link HRule}
     */
    HRule rule();

    /**
     * 模型类型，包含以下几种：
     *
     * <pre><code>
     *     - DIRECT：直接模型，所有属性都在内部模型中直接定义，且映射到单独的表结构中。
     *     - JOINED：跨表模型，底层映射到多张表的结构中。
     *     - VIEW：视图模型，底层映射到单独的视图结构中。
     *     - READONLY：只读模型（只读数据）
     * </code></pre>
     *
     * @return {@link EmModel.Type}
     */
    EmModel.Type type();

    /**
     * 返回当前模型中的属性集，通常是遍历时专用，配合提取 {@link HAttribute} 的方法
     *
     * @return {@link Set}
     */
    Set<String> attribute();


    /**
     * 不论哪种模型中的属性都可以挂载属性通道
     * <pre><code>
     *     输入通道：
     *     - {@link io.modello.specification.uca.IComponent}
     *     - {@link io.modello.specification.uca.INormalizer}
     *     输出通道：
     *     - {@link io.modello.specification.uca.OComponent}
     *     - {@link io.modello.specification.uca.OExpression}
     * </code></pre>
     *
     * @param name 属性名
     *
     * @return {@link HAttribute}
     */
    HAttribute attribute(String name);

    /**
     * 当前模型所属的应用信息，用于捕捉底层的应用描述相关信息
     *
     * @return {@link HArk}
     */
    HArk ark();

    /**
     * 模型本身的元模型附加信息
     * <pre><code>
     *     1. 子模型元数据
     *     2. 复杂定义
     *     3. 类型定义
     *     4. 顶层抽象定义
     * </code></pre>
     *
     * @return {@link KMarkAtom}
     */
    KMarkAtom marker();

    /**
     * 当前模型的引用信息，用于定义和模型相关的所有引用定义。
     * <pre><code>
     *     1. 主引用信息
     *     2. 辅助引用
     *     3. 集成引用
     *     4. 外置存储引用
     * </code></pre>
     *
     * @return {@link HReference}
     */
    HReference reference();

    /**
     * 正如 {@link HAtom} 内部引用了 {@link HModel} 的定义，{@link HModel} 也可以引用 {@link HEntity} 的定义。
     * 三层结构从此处而来，新版中 {@link HEntity} 中定义了最底层的结构信息：
     * <pre><code>
     *     1. 模型对应的实体类型（语言级）
     *     2. 抽象字段定义（语言级）
     *     3. 接口定义
     *     4. 键定义
     * </code></pre>
     * 此处处理模型统一的归口定义，在底层实现过程中可实现完整的语言级模型定义，zero-atom 的扩展框架中不带有任何语言级定义，
     * 但是它提供了一个参考，基于完整的标准规范直接在数据库中有所定义，如基础概念：
     * <pre><code>
     *     1. Entity：实体表模型
     *     2. Join：模型和实体关联（多对多）的链接模型
     *     3. Key：实体的键相关信息
     *     4. Field：实体的属性信息
     *     5. Index：实体的索引信息
     * </code></pre>
     * 内部使用 {@link HJoin} 计算关联关系
     *
     * @return {@link ConcurrentMap}
     */
    default ConcurrentMap<String, HEntity> entity() {
        return new ConcurrentHashMap<>();
    }
}
