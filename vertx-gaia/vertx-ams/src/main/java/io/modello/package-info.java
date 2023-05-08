/**
 * 建模专用子包，用于兼容几种不同的模型：
 * <pre><code>
 * 1. 静态模型（带有Java Class定义的模型）
 * 2. 动态模型（无Class定义的模型）
 * 3. EMF建模（连接了EMF基础规范的模型）
 * </code></pre>
 * 基础元素模型（基于底座）
 * <pre><code>
 * - 类型定义：{@link io.modello.specification.element.HType}
 * </code></pre>
 */
package io.modello;