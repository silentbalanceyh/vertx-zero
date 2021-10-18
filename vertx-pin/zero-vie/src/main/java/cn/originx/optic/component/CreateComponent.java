package cn.originx.optic.component;

import cn.originx.scaffold.component.AbstractAdaptor;
import io.vertx.core.Future;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;
import io.vertx.up.commune.Record;

/**
 * ## 「Channel」创建记录通道
 *
 * ### 1. 基本介绍
 *
 * 不带任何<strong>集成功能</strong>的标准创建组件，支持如下功能：
 *
 * - 传入数据，直接插入模型对应数据
 * - 请求`POST /api/ox/:identifier`
 *
 * ### 2. 通道详细
 *
 * - 类型：ADAPTOR
 *
 * ### 3. 请求格式
 *
 * ```json
 * // <pre><code class="json">
 * {
 *     "key": "主键",
 *     "name": "名称",
 *     "field2": "...",
 *     "field3": "..."
 * }
 * // </code></pre>
 * ```
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
public class CreateComponent extends AbstractAdaptor {

    /**
     * 「Async」通道主方法
     *
     * @param request 通道的标准请求参数，类型{@link ActIn}。
     *
     * @return 返回`{@link Future}<{@link ActOut}>`
     */
    @Override
    public Future<ActOut> transferAsync(final ActIn request) {
        final Record record = this.activeRecord(request);
        return this.dao().insertAsync(record)
            .compose(ActOut::future);
    }
}
