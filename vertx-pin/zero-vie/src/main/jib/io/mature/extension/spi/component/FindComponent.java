package io.mature.extension.spi.component;

import io.mature.extension.scaffold.stdn.AbstractHOne;
import io.vertx.core.Future;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;

/**
 * ## 「Channel」读取数据通道
 *
 * ### 1. 基本介绍
 *
 * - 根据 key 值读取记录集
 * - 请求`GET /api/ox/:identifier/:key`
 *
 * ### 2. 通道详细
 *
 * - 类型：ADAPTOR
 *
 * ### 3. 请求格式
 *
 * 无请求体（Body）数据，直接传入identifier读取。
 *
 * ### 4. 响应格式
 *
 * ```json
 * // <pre><code class="json">
 * {
 *     "data": {
 *          "key": "主键",
 *          "name": "名称",
 *          "field2": "...",
 *          "field3": "..."
 *     }
 * }
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FindComponent extends AbstractHOne {
    @Override
    public Future<ActOut> transferAsync(final ActIn request) {
        final String key = this.activeKey(request);
        return this.fetchFull(key).compose(ActOut::future);
    }
}
