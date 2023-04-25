package cn.originx.spi.component;

import cn.originx.scaffold.stdn.AbstractHMore;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;

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
public class BatchDeleteComponent extends AbstractHMore {

    /**
     * 「Async」通道主方法
     *
     * @param request 通道的标准请求参数，类型{@link ActIn}。
     *
     * @return 返回`{@link Future}<{@link ActOut}>`
     */
    @Override
    public Future<Apt> transferIn(final ActIn request) {


        /*
         * 读取当前输入记录中的所有主键集合，该操作最终执行如：
         *
         * 1. 执行SQL语句：DELETE FROM XXX WHERE KEY IN (?,?,?,...)
         * 2. 提取的主键属性为`key`
         * 3. 执行调用时读取父类全数据 fetchFull 操作
         * */
        final String[] keys = this.activeKeys(request);
        return this.fetchFull(keys)


            /*
             * 构造 Apt 对象
             * 1. 旧数据有值
             * 2. 新数据为 null
             */
            .compose(Apt::inDelete);
    }

    @Override
    public Future<JsonArray> transferAsync(final Apt apt, final ActIn request, final DataAtom atom) {
        return apt.<JsonArray>dataOAsync()

            /* 1. Trash 历史备份 */
            .compose(data -> this.backupAsync(data, atom))

            /* 2. 模型 */
            .compose(this.completer(atom)::remove)

            /* 3. 生成删除专用的变更历史 */
            .compose(oldArray -> this.trackAsyncD(oldArray, atom));
    }
}
