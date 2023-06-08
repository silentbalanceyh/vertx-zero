package io.modello.specification.element;

import io.macrocosm.specification.app.HNamespace;

/**
 * 「包」
 * 语言专用包定义相关信息，包和名空间存在继承结构
 * <pre><code>
 *     1. {@link HNs#name()} 名空间名称
 *     2. {@link HNs#nsPrefix()} XML专用的名空间前缀信息
 *     3. {@link HNs#nsUri()} XML专用的名空间URI信息
 *     4. {@link HNamespace#domain()} 所属域相关信息，直接和名空间绑定的域
 * </code></pre>
 *
 * @author lang : 2023-05-22
 */
public interface HPackage extends HNamespace {
    /**
     * 包的别名，当名空间名称不足以描述包名时，需启用别名
     * <pre><code>
     *     1. XML部分使用 name 方法
     *     2. Java部分使用 identifier 方法
     * </code></pre>
     *
     * @return {@link String}
     */
    String identifier();
}
