package io.modello.eon.em;

/**
 * 复杂格式的格式定义，系统中定义了三种复杂格式：
 * <pre><code>
 *     1. Elementary：基础格式，常用的Java toString 类型。
 *     2. JsonObject：Json对象格式，默认为 {}。
 *     3. JsonArray：Json数组格式，默认为 []。
 *     4. Expression：追加一种表达式格式，用于脚本计算。
 * </code></pre>
 * 格式定义的配置Json段如下：
 * <pre><code>
 *     {
 *         "source": "JsonArray | JsonObject | Elementary",
 *         "fields": []
 *     }
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum ValueFormat {
    // Json数组
    JsonArray,
    // Json对象
    JsonObject,
    // 基础格式
    Elementary,
    // 表达式（SMAVE才会使用）
    Expression,
}
