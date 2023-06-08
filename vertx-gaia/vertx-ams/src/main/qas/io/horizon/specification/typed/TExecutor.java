package io.horizon.specification.typed;

import io.vertx.core.json.JsonObject;

/**
 * 「执行器」
 * <hr/>
 * 高阶执行器组件，包含了组件常用的执行器方法，用于执行器的基础定义。
 * <pre><code>
 *     1. component：定义了执行器类
 *     2. config：定义了执行器配置
 * </code></pre>
 *
 * @author lang : 2023-05-21
 */
public interface TExecutor {

    default Class<?> component() {
        return null;
    }

    default JsonObject config() {
        return new JsonObject();
    }
}
