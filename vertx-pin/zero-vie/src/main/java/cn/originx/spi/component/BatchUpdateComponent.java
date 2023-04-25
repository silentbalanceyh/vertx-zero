package cn.originx.spi.component;

import cn.originx.scaffold.stdn.AbstractHMore;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;

/**
 * ## 「Channel」批量更新通道
 *
 * ### 1. 基本介绍
 *
 * 不带任何<strong>集成功能</strong>的标准批量更新组件，支持如下功能：
 *
 * - 批量更新（只更新传入的字段）
 * - 请求`PUT /api/ox/batch/:identifier/update`
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
 *      {
 *          "field1": "value1",
 *          "field2": "value2",
 *          "...": "..."
 *      },
 *      {
 *          "field1": "value1",
 *          "field2": "value2",
 *          "...": "..."
 *      }
 * ]
 * // </code></pre>
 * ```
 *
 * ### 4. 响应格式
 *
 * ```json
 * // <pre><code class="json">
 * {
 *     "data":[
 *          {
 *              "field1": "value1",
 *              "field2": "value2",
 *              "...": "..."
 *          },
 *          {
 *              "field1": "value1",
 *              "field2": "value2",
 *              "...": "..."
 *          }
 *      ]
 * }
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BatchUpdateComponent extends AbstractHMore {

    /**
     * 「Async」通道主方法
     *
     * // <pre><code class="json">
     * {
     *      "keys": [
     *          "key1", "key2", "..."
     *      ],
     *      "data": {
     *          "field1": "value1",
     *          "field2": "value2"
     *      }
     * }
     * // </code></pre>
     *
     * @param request 通道的标准请求参数，类型{@link ActIn}。
     *
     * @return 返回`{@link Future}<{@link ActOut}>`
     */
    @Override
    public Future<Apt> transferIn(final ActIn request) {
        final String[] keys = this.inKeys(request);
        final JsonObject bodyData = this.inDataJ(request);

        /*
         * 读取当前输入记录中的所有主键集合，该操作最终执行如：
         *
         * 1. 执行SQL语句：SELECT * FROM XXX WHERE KEY IN (?,?,?,...)
         * 2. 提取的主键属性为`keys`
         * 3. 执行调用时读取父类全数据 fetchFull 操作
         * */
        return this.fetchFull(keys)

            /*
             * 构造 Apt 对象
             * 1. 新旧数据同值
             * 2. 按 bodyData 中的值处理更新
             */
            .compose(Apt::inEdit)

            /* 更新数据 */
            .compose(apt -> apt.updateAsync(bodyData));
    }

    @Override
    public Future<JsonArray> transferAsync(final Apt apt, final ActIn request, final DataAtom atom) {
        return apt.<JsonArray>dataIAsync()

            /* 1. 模型 */
            .compose(this.completer(atom)::update)

            /* 2. 生成更新专用的变更历史 */
            .compose(newArray -> this.trackAsyncU(apt.dataO(), newArray, atom));
    }
}
