package io.horizon.specification.action;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * 「六维执行接口」
 * 可以在不同模块中使用，参考六个核心维度：
 * <pre><code>
 *     1. 输入执行维度
 *        T -> 核心DTO
 *        JsonObject -> 单记录执行
 *        JsonArray -> 多记录执行
 *     2. 行为维度
 *        Sync -> 同步执行（默认方式）
 *        Async -> 异步执行
 *     最终转换成 2 x 3 的六维度处理
 * </code></pre>
 *
 * @author lang : 2023-06-03
 */
public interface HDo<I, O> extends HDoJ<O> {

    /**
     * （核心DTO）专用检查方法，检查数据结果是否符合预期
     *
     * @param data   数据
     * @param config 配置
     *
     * @return {@link O}
     */
    O execute(I data, JsonObject config);

    /**
     * 「异步版本」（核心DTO）专用检查方法，检查数据结果是否符合预期
     *
     * @param data   数据
     * @param config 配置
     *
     * @return {@link Future}
     */
    default Future<O> executeAsync(final I data, final JsonObject config) {
        return Future.succeededFuture(this.execute(data, config));
    }
}

