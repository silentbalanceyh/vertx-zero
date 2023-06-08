package io.horizon.specification.uca;

import io.horizon.specification.action.HDo;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;

/**
 * 「分流器」
 * 可以在不同的模块中使用，主要用于配置模块筛选 {@link String} 类型的 identifier 专用，或Map配置下的 key 专用
 * <pre><code>
 *     1. AOP分流器
 *     2. 新版标识规则选择器
 * </code></pre>
 * 分流器会使用泛型T以及 JSON 格式优先的模式进行处理，主要是筛选一个键用来做配置分流行为。
 *
 * @author lang : 2023-06-03
 */
public interface HRobin<T> extends HDo<T, String> {

    @Override
    default String execute(final T data, final JsonObject config) {
        return null;
    }

    @Override
    default String executeJ(final ClusterSerializable data, final JsonObject config) {
        return null;
    }

}
