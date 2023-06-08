package io.mature.extension.spi.component;

import io.mature.extension.scaffold.stdn.AbstractHOne;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.record.Apt;

/**
 * ## 「Channel」删除记录通道
 *
 * ### 1. 基本介绍
 *
 * 不带任何<strong>集成功能</strong>的标准删除组件，支持如下功能：
 *
 * - 根据 key 删除某条记录
 * - 请求`DELETE /api/ox/:identifier/:key`
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
 *     "data": true
 * }
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DeleteComponent extends AbstractHOne {

    @Override
    public Future<Apt> transferIn(final ActIn request) {
        final String key = this.activeKey(request);
        /*
         * SELECT * FROM XXXX WHERE KEY = ?
         */
        return this.fetchFull(key)


            /*
             * 构造 Apt 对象
             * 1. 旧数据有值
             * 2. 新数据为 null
             */
            .compose(Apt::inDelete);

    }

    @Override
    public Future<JsonObject> transferAsync(final Apt apt, final ActIn request, final DataAtom atom) {
        return apt.<JsonObject>dataIAsync()

            /* 1. Trash 历史备份 */
            .compose(data -> this.backupAsync(data, atom))

            /* 2. 模型 */
            .compose(this.completer(atom)::remove)

            /* 3. 生成变更历史 */
            .compose(oldRecord -> this.trackAsyncD(oldRecord, atom));

    }
}
