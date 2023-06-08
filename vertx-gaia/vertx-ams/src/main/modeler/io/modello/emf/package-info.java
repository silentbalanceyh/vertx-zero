/**
 * EMF 2.33.0 规范新版本处理，基本原则如下：
 * <pre><code>
 *     1. {@link io.modello.emf.typed} 为抽象类型定义包，定义了所有 abstract 抽象类型。
 *     2. {@link io.modello.emf.specification} 为抽象接口定义包，定义了所有 interface 抽象类型。
 *     3. 主包中定义常量信息。
 * </code></pre>
 * 类名相关信息的基本规范如下：
 * <pre><code>
 *     1. 前缀 Name：定义了所有 EMF 中属性相关枚举类型。
 *     2. 前缀 V：常量定义。
 *     3. 前缀 M：规范和抽象类定义。
 * </code></pre>
 */
package io.modello.emf;