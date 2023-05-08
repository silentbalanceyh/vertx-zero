package io.modello.specification.rule;

import io.modello.specification.app.HApp;

/**
 * 名空间定义，定义对应名空间的规范相关信息，当一个元素转换成标准时，一定会包含名空间的引用，名空间引用使用组合模式（非继承），不同名空间
 * 的元素定义会有所区别，对应于XML中的如下抽象定义：
 * <pre><code class="xml">
 *     <myNS nsURI="" nsPrefix="">
 *     </myNS>
 * </code></pre>
 *
 * 所以名空间中通常会包含如下元素集：
 *
 * <ol>
 *     <li>name：表示名空间本身的名称，对应下层的所属</li>
 *     <li>uri：当前名空间绑定的URI，可转换成 nsURI 部分</li>
 *     <li>prefix：当前名空间使用的默认的前缀，通常是 XML 中的前缀信息，默认为 ""</li>
 * </ol>
 *
 * 和原始建模项目的区别 zero-atom，zero-atom 是不跨应用部分的建模处理，而高阶建模部分是跨应用部分的建模，该模型由于包含了 uri 部分，
 * Domain部分的区别直接限定了其名空间信息，该接口和 {@link HApp} 接口的属性含义和对应关系：
 *
 * <pre><code>
 * 1. HApp 用于描述模型所属应用，之中包含了
 *    - resource(): 模型/实体, 绑定的资源目录
 *    - identifier(): 统一标识符
 *    - namespace(): 模型所属名空间
 * 2. HNS 用于描述所有元素（不仅限模型）的名空间，其中包括
 *    - name(): 元素所属名空间，和 HApp 中的 namespace() 对应
 *    - uri(): 元素名空间所对应的 XML 中的引用 uri 地址
 *    - prefix(): 元素名空间对应的 XML 中的 nsPrefix 前缀定义
 * </code></pre>
 *
 * @author lang : 2023-05-08
 */
public interface HNs {
    /**
     * @return 名空间名称
     */
    String name();

    /**
     * @return 名空间的URI地址（全网唯一）
     */
    String uri();

    /**
     * @return 名空间的默认前缀
     */
    String prefix();
}
