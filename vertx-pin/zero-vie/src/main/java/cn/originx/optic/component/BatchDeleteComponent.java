package cn.originx.optic.component;

import cn.originx.scaffold.component.AbstractAdaptor;
import io.vertx.core.Future;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;
import io.vertx.up.commune.Record;

/**
 * ## 「Channel」批量删除通道
 *
 * ### 1. 基本介绍
 *
 * 不带任何<strong>集成功能</strong>的标准批量删除组件，支持如下功能：
 *
 * - 根据`key`集合删除记录集
 * - 请求`DELETE /api/ox/batch/:identifier/delete`
 *
 * ### 2. 通道详细
 *
 * - 类型：ADAPTOR
 *
 * ### 3. 请求格式
 *
 * ```json
 * // <pre><code class="json">
 * [
 *      "key1",
 *      "key2"
 * ]
 * // </code></pre>
 * ```
 *
 * ### 4. 响应格式
 *
 * ```json
 * // <pre><code class="json">
 * {
 *     "data": true
 * }
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BatchDeleteComponent extends AbstractAdaptor {

    /**
     * 「Async」通道主方法
     *
     * @param request 通道的标准请求参数，类型{@link ActIn}。
     *
     * @return 返回`{@link Future}<{@link ActOut}>`
     */
    @Override
    public Future<ActOut> transferAsync(final ActIn request) {
        /* 和 DataAtom 绑定 */
        final Record[] records = this.activeRecords(request);
        /* 读取主键 */
        return this.dao().deleteAsync(records)
            .compose(ActOut::future);
    }
}
