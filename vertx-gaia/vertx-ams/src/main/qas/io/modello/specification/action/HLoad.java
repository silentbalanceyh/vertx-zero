package io.modello.specification.action;

import io.modello.specification.atom.HAtom;

/**
 * 统一模型加载专用接口，用于加载不同的模型，其中包括：
 * <pre><code>
 *     1. DataAtom，位于 zero-atom 项目中的基本动态建模核心模型。
 *     2. NormAtom，使用 Hybrid 模式处理的静态混合相容的混合模型（现阶段主要使用于流程工单）。
 *     3. EmfAtom，使用 EMF 模式处理的动态核心模型（内部对接Emf规范），不开源不对外。
 * </code></pre>
 *
 * 「统一模型」{@link HAtom}
 * 1. 关于标识规则
 * {@link HAtom} 内部包含了 {@link io.modello.specification.atom.HRule} 定义的双通道标识规则：
 * <pre><code>
 *     - 来自 {@link io.modello.specification.atom.HModel} 内置的模型定义中的标识规则，
 *       又称为 Master 的 {@link io.modello.specification.atom.HRule}，作为模型定义的主标识规则。
 *     - 来自 {@link io.modello.specification.action} 包内定义的行为标识规则，不同行为的标识规则定义
 *       跟着基础行为定义会有所不同，标识规则绑定来自编程过程中调用 connect 的API接口。
 *     - {@link io.modello.specification.atom.HRule} 标识规则支持嵌套结构：
 *       {
 *           "...": "内容（根）",
 *           "children": {
 *               "identifier1": {},
 *               "identifier2": {}
 *           }
 *       }
 *       嵌套结构标识的使用在后期会提供标识规则选择器选择核心标识规则。
 * </code></pre>
 *
 * 2. 关于标准模型
 * <pre><code>
 *    「动态模型」DataAtom
 *    每次创建一个新的 DataAtom，属于 Model 的聚合体，它的核心两个数据结构主要是 AoPerformer (HPerformer<Model>) 和 Model (HModel)，
 *    这两个结构在底层做了池化处理，也就是说：访问器 和 模型（底层）都不会出现多次创建的情况，那么为什么 DataAtom 要创建多个呢？
 *    主要原因是 DataAtom 开始出现了分离。
 *
 *    「静态模型」NormAtom
 *    静态模型和动态模型有一定区别，静态模型属于只读模型，即属性本身不可变更，每次容器启动就注定了该模型的基础信息，新架构
 *    下底层核心数据结构是 HPerformer 和 HModel，这两个结构本身就在底层做了池化处理，所以同样的可以直接创建新的 NormAtom
 *    对象，和 DataAtom 一样，此处会有模型分离。
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HLoad {

    /*
     * 「统一模型」HAtom
     * HAtom 中的 RuleUnique 包含了两部分内容
     *
     * 1. RuleUnique 来自 Model （模型定义），Master 的 RuleUnique
     * 2. RuleUnique 来自 connect 编程模式，Slave 的 RuleUnique
     * 3. 每个 RuleUnique 的结构如
     *
     * {
     *      "...":"内容（根）",
     *      "children": {
     *          "identifier1": {},
     *          "identifier2": {}
     *      }
     * }
     *
     * 通道如果是静态绑定，则直接使用 children 就好
     * 而通道如果出现了动态绑定，IdentityComponent 实现，则要根据切换过后的 HAtom 对应的 identifier 去读取相对应的标识规则。
     *
     * 所以，每次get的时候会读取一个新的 HAtom 而共享其他数据结构。其他数据结构包括内部的
     * - 「加载器」HPerformer
     * - 「模型」HModel
     * - （内部）「基础辅助器」HAtomMetadata
     * - （内部）「引用辅助器」HAtomReference
     * - （内部）「标识规则辅助器」HAtomUnique
     *
     * 「动态模型」DataAtom
     * 每次创建一个新的 DataAtom
     * - DataAtom 属于 Model 的聚合体，它的核心两个数据结构主要是 AoPerformer (HPerformer<Model>) 和 Model (HModel)，
     * 这两个结构在底层做了池化处理，也就是说：访问器 和 模型（底层）都不会出现多次创建的情况，那么为什么 DataAtom 要创建多个呢？
     * 主要原因是 DataAtom 开始出现了分离。
     *
     * 「静态模型」NormAtom
     * 静态模型和动态模型有一定区别，静态模型属于只读模型，即属性本身不可变更，每次容器启动就注定了该模型的基础信息，新架构
     * 下底层核心数据结构是 HPerformer 和 HModel，这两个结构本身就在底层做了池化处理，所以同样的可以直接创建新的 NormAtom
     * 对象，和 DataAtom 一样，此处会有模型分离。
     *
     * 所以最终形成的池化结构如（组件缓存）：
     * - HPerformer「线程级」,       key = <应用名称>
     * - HModel「内存级」,           key = <名空间 + identifier>
     * - HAtom：每次都创建新的
     * - HAtomUnique
     *   HAtomMetadata
     *   HAtomReference
     *   AtomMarker（DataAtom专享）
     *   上述四种组件的 key = HModel.hashCode()，等价于：key = <名空间 + identifier>
     *
     * 假设环境中有 32 线程，1个名空间，15个模型，最终的对象数量（满）为：
     * - HPerformer:                    32
     *   HModel:                        15
     *   HAtom:                         N
     *   HAtomUnique                    15
     *   HAtomMetadata                  15
     *   HAtomReference                 15
     *   AtomMarker ( DataAtom专享 )     15
     *
     * HAtomUnique在此处数量虽然有限定，但其实最终形成的RuleUnique是双份：
     * - HAtomUnique（固定）             15
     *   HAtomUnique（绑定、动态）        N        （编程中绑定通道内部标识规则，瞬时连接）
     */
    HAtom atom(String appName, String identifier);
}
