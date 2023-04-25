package cn.originx.spi.component;

import cn.originx.scaffold.component.AbstractAdaptor;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;

/**
 * ## 「Channel」搜索通道
 *
 * ### 1. 基本介绍
 *
 * 不带任何<strong>集成功能</strong>的标准搜索通道，支持如下功能
 *
 * - 查询不同于其他接口，直接从 request 中提取 query
 * - 请求`POST /api/ox/:identifier/search`
 *
 * ### 2. 通道详细
 *
 * - 类型：ADAPTOR
 * - Qr参数
 *
 * ### 3. 请求格式
 *
 * > 参考Zero查询引擎中的Qr标准格式。
 *
 * ```json
 * // <pre><code class="json">
 * {
 *     "criteria":{
 *          "field1,=": "value1",
 *          "$op":{
 *              "field2,<": "value2",
 *              "field2,>": "value3"
 *          }
 *     },
 *     "sorter":[
 *          "field1,ASC",
 *          "field2,DESC"
 *     ],
 *     "projection": [
 *     ],
 *     "pager":{
 *         "page": "页码",
 *         "size": "页尺寸"
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
 *     "data": {
 *          "count": "记录总数",
 *          "list": [
 *              {
 *                  "...": "..."
 *              }
 *          ]
 *     }
 * }
 * // </code></pre>
 * ```
 *
 * > 可支持服务端分页、查询、排序逻辑。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SearchComponent extends AbstractAdaptor {

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
        final JsonObject query = request.getQuery();
        return this.dao().searchAsync(query)
            /*
             * 分页过后的最终结果
             */
            .compose(ActOut::future);
    }
}
