package io.horizon.specification.uca;

import io.horizon.specification.action.HDo;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;

/**
 * 「检查器」
 * 可以在不同的模块中使用，主要用于检查数据本身的合法性，核心使用场景：
 * <pre><code>
 *     1. 请求拦截器：可配置到 AOP 层直接拦截请求，检查请求的合法性
 *     2. 检查结果通常会以异步的方式处理
 *        - 成功：{@link io.vertx.core.Future#succeededFuture(Object)}
 *        - 失败：{@link io.vertx.core.Future#failedFuture(Throwable)}
 *     3. 不仅如此，检查的最终结果会直接返回输入信息，方便后续的处理
 * </code></pre>
 *
 * @author lang : 2023-05-27
 */
public interface HTrue<T> extends HDo<T, Boolean> {

    @Override
    default Boolean execute(final T data, final JsonObject config) {
        return Boolean.TRUE;
    }

    @Override
    default Boolean executeJ(final ClusterSerializable data, final JsonObject config) {
        return Boolean.TRUE;
    }
}

