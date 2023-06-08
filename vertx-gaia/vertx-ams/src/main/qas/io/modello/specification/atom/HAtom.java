package io.modello.specification.atom;

import io.macrocosm.specification.program.HArk;
import io.modello.atom.normalize.KMarkAtom;
import io.modello.specification.action.HDiff;
import io.modello.specification.meta.HMetaAtom;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * Zero中的模型结构处理，主要包括：
 * <pre><code>
 * -- Zero核心框架建模（Hybrid建模方式）
 * -- Zero扩展框架建模（zero-atom中的动态建模方式）
 * -- Zero规范建模（EMF建模，依赖Ecore模型）
 * </code></pre>
 *
 * 此处提供了所有基于Zero中支持的不同种类的模型定义：
 * <pre><code>
 * -- 动态模型：
 *    内置于扩展模块（ zero-atom ）中的 `M_X` 系列表结构存储元模型数据的建模，该模型的子类将被转换为
 *    DataAtom 类型
 *
 * -- 静态模型：
 *    （依赖代码生成）Jooq生成（Pojo/Dao）模块，是Zero框架中的标准模块，面相开发人员的常用建模。
 *
 * -- 集成模型：
 *    针对系统集成的第三方建模方式，可直接开启 模型通道，针对属性进行强定制。
 *
 * -- 工业模型：
 *    可直接转换成工业规范的模型，提供工业规范序列化，如 ISO-XXXXX，实现工业规范的完整对接，直接将模型
 *    转换成工业标准实现对接，核心建模框架直接使用 EMF。
 *
 * -- 采集模型：
 *    对接 JPython 或 Python 实现数据分析统一模型，后续会形成子接口对接数据分析核心算法，搭载分析模块
 *    针对常用业务场景实现数据分析流程。
 *
 * -- 流程模型：
 *    对接 Camunda 流程引擎（Hybrid混合方式），针对工作流中的模型进行可拓展管理模式的核心模型
 *
 * -- 监控模型（面向运维）：
 *    对接 K8S 的监控平台实现针对模型本身的前端建模方式，可监控模型的所有变化，实现通知和变化感知
 *
 * -- 设计模型（面向设计）：
 *    实现低代码所见即所得的设计界面交互模式的模型，可直接在界面中进行模型的设计，实现低代码的可视化
 *
 * -- 开发模型（面向开发）：
 *    在系统中直接提供调试、断点、语言级的建模方式，实现面相开发人员的建模。
 *
 * </code></pre>
 *
 * @author lang
 */
public interface HAtom extends
    HDiff,                  // 模型比较器，返回当前模型的 Vs 接口
    HAtomAttribute,         // 模型属性专用接口
    HAtomRule,              // 标识规则专用接口
    HAtomIo                 // 属性标识符专用接口
{

    /**
     * 基于当前模型定义创建模型副本，该副本和模型定义拥有同样的 namespace，但是拥有不同的 identifier
     * 快速创建模型定义的专有方法，而且防止模型副本本身被改动。
     *
     * @param identifier 模型副本的标识符
     *
     * @return 模型副本
     */
    HAtom copy(String identifier);

    // =================== Cross Method between two atoms ===========

    /**
     * （Metadata）返回当前模型定义中的元模型信息，里面包含了核心建模内置定义
     *
     * @return 元模型信息
     */
    HMetaAtom shape();

    /**
     * （Modeler）返回当前模型定义中的元模型消费信息，里面包含了消费层所需的真实模型定义
     *
     * @param <T> 模型类型
     *
     * @return 消费模型信息
     */
    <T extends HModel> T model();

    // =================== 模型元数据基础 ===========
    String identifier();

    HArk ark();

    // ==================== Reference Information ================
    HReference reference();
}


// ==================== 属性标记 =====================

/**
 * 返回模型的属性标记
 */
interface HAtomIo {
    /**
     * 直接引用模型级的属性标记类，记录了当前模型两级配置
     * <pre><code>
     *     1. 模型是否可追踪
     *     2. 在可追踪和不可追踪模式下的属性标记信息（每个属性中会包含 {@link io.modello.atom.normalize.KMarkAttribute}）
     * </code></pre>
     *
     * @return {@link KMarkAtom}
     */
    KMarkAtom marker();
}
// ==================== 标识规则 =====================

/**
 * 返回模型中不同标识规则之下的内容
 */
interface HAtomRule {

    /**
     * 「低优先级」返回模型的约束信息，此处返回约束为 Master 约束，通常是配置在元模型仓库中
     * （存储在数据库），但该约束的优先级会低于通道级的约束，允许部分行为对其执行变更。
     *
     * @return {@link HRule}
     */
    HRule ruleAtom();

    /**
     * 「高优先级」返回模型的通道级约束，同一个模型在不同通道中的约束信息可以不相同，通常是
     * 编程模式可变更的动态约束，例如：在不同通道中，同一个模型的属性名可以不同。
     *
     * @return {@link HRule}
     */
    HRule rule();

    /**
     * 标识规则搜索模式，动态返回当前模型启用的标识规则：
     * <pre><code>
     *     1. 如果当前模型启用了通道级的标识规则，则返回通道级的标识规则
     *     2. 如果当前模型没有启用通道级的标识规则，则返回模型级的标识规则
     * </code></pre>
     *
     * @return {@link HRule}
     */
    HRule ruleSmart();

    /**
     * 该方法仅会在执行器部分被调用，主要用于动态绑定当前环境生效的标识规则
     *
     * @param rule 标识规则
     * @param <T>  标识规则类型
     *
     * @return {@link HRule}
     */
    <T extends HAtom> T rule(HRule rule);
}

// ==================== 属性部分 =====================

/**
 * Atom模型的属性部分，用于描述模型的属性相关信息
 *
 * @author lang
 */
interface HAtomAttribute {
    /**
     * 返回当前模型的所有属性名
     *
     * @return 属性名集合
     */
    Set<String> attribute();

    /**
     * 返回属性名的UI呈现信息 name = alias
     * 移除旧接口：
     * <pre><code>
     *     / String alias(String name)
     * </code></pre>
     *
     * @return 属性名集合
     */
    ConcurrentMap<String, String> alias();

    /**
     * 返回当前模型指定的属性名 {@link HAttribute}
     *
     * @param name 属性名
     *
     * @return 属性
     */
    HAttribute attribute(String name);

    /**
     * 返回所有属性类型信息
     * 移除旧接口：
     * <pre><code>
     *     / Class<?> type(String name);
     * </code></pre>
     *
     * @return 类型集合
     */
    ConcurrentMap<String, Class<?>> type();
}
