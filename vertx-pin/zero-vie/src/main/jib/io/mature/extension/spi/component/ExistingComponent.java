package io.mature.extension.spi.component;

import io.horizon.uca.qr.Criteria;
import io.mature.extension.scaffold.component.AbstractAdaptor;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;

/**
 * ## 「Channel」存在检查通道
 *
 * ### 1. 基本介绍
 *
 * 不带任何<strong>集成功能</strong>的标准存在检查通道，支持如下功能
 *
 * - 传入查询引擎中的`criteria`复杂条件
 * - 请求`POST /api/ox/:identifier/existing`
 *
 * ### 2. 通道详细
 *
 * - 类型：ADAPTOR
 * - Qr参数中的`criteria`部分
 *
 * ### 3. 请求格式
 *
 * > 参考Zero查询引擎中的Qr标准格式。
 *
 * ```json
 * // <pre><code class="json">
 * {
 *     "field1,=": "value1",
 *     "$op":{
 *         "field2,<": "value2",
 *         "field2,>": "value3"
 *     }
 * }
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
 * - true：表示当前数据记录存在于数据库中。
 * - false：当前数据记录不存在。
 *
 * 该接口通常应用于表单中的重复记录检查逻辑。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExistingComponent extends AbstractAdaptor {
    /**
     * 「Async」通道主方法
     *
     * @param request 通道的标准请求参数，类型{@link ActIn}。
     *
     * @return 返回`{@link Future}<{@link ActOut}>`
     */
    @Override
    public Future<ActOut> transferAsync(final ActIn request) {
        /* 读取主键 */
        final JsonObject data = request.getJObject();
        final Criteria criteria = Criteria.create(data);
        return this.dao().existAsync(criteria)
            .compose(ActOut::future);
    }
}
