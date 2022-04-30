package cn.originx.optic.component;

import cn.originx.refine.Ox;
import cn.originx.scaffold.component.AbstractAdaptor;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.optic.ui.ApeakMy;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;

/**
 * ## 「Channel」我的列读取通道
 *
 * ### 1. 基本介绍
 *
 * 该通道又称为<strong>个人视图通道</strong>，不带任何<strong>集成功能</strong>的标准读取我的列组件，支持如下功能：
 *
 * - 根据`identifier`读取我的可访问属性列数据。
 * - 该属性列构造会对全列数据执行过滤，实现表格的列过滤功能。
 * - 请求`GET /api/ox/columns/:identifier/my`
 *
 * ### 2. 通道详细
 *
 * - 类型：ADAPTOR
 * - 直接从`DataAtom`中读取模型标识符
 * - 内置调用了ServiceLoader通道`io.vertx.tp.optic.ui.ApeakMy`
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
 *      "data": [
 *          "column1",
 *          "column2",
 *          "..."
 *      ]
 * }
 * // </code></pre>
 * ```
 *
 * 由于<strong>我的列</strong>读取过程中会出现<strong>二种</strong>不同的后续逻辑，当前组件中多出了下边代码：
 *
 * ```java
 * // <pre><code class="java">
 *     .compose(params -> Ux.channelAsync(ApeakMy.class,
 *          () -> ActOut.future(new JsonArray()),
 *          stub -> stub.fetchMy(params).compose(ActOut::future)
 *     ))
 * // </code></pre>
 * ```
 *
 * 内置调用{@link Ox#viewMy Ox.viewMy}
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ColumnMyComponent extends AbstractAdaptor {

    /**
     * 「Async」通道主方法
     *
     * @param request 通道的标准请求参数，类型{@link ActIn}。
     *
     * @return 返回`{@link Future}<{@link ActOut}>`
     */
    @Override
    public Future<ActOut> transferAsync(final ActIn request) {
        /* 根据 sigma 读取应用环境 */
        final Envelop envelop = request.getEnvelop();
        /* 最终Uri */
        final DataAtom atom = this.atom();
        /* 前置一致 */
        return Ox.viewMy(envelop, atom.identifier()).compose(params -> Ux.channelAsync(ApeakMy.class,
            () -> ActOut.future(new JsonArray()),
            stub -> stub.fetchMy(params).compose(ActOut::future)
        ));
    }
}
