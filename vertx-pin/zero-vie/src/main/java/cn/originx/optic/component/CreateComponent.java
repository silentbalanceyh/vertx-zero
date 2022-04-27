package cn.originx.optic.component;

import cn.originx.scaffold.stdn.AbstractHOne;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.commune.ActIn;
import io.vertx.up.unity.Ux;

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
public class CreateComponent extends AbstractHOne {
    @Override
    public Future<JsonObject> transferAsync(final Apt apt, final ActIn request, final DataAtom atom) {

        return apt.<JsonObject>dataIAsync()

            /* 1. 压缩数据 */
            .compose(data -> Ux.future(this.diffAdd(data, atom)))

            /* 2. 模型 */
            .compose(this.completer(atom)::create)

            /* 3. 生成添加专用的变更历史 */
            .compose(newRecord -> this.trackAsyncC(newRecord, atom));
    }
}
