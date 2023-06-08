package io.mature.extension.spi.component;

import io.horizon.spi.ui.ApeakMy;
import io.mature.extension.refine.Ox;
import io.mature.extension.scaffold.component.AbstractAdaptor;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.secure.Vis;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/**
 * ## 「Channel」我的列定制通道
 *
 * ### 1. 基本介绍
 *
 * 该通道又称为<strong>个人视图保存通道</strong>，不带任何<strong>集成功能</strong>的标准存储我的视图组件，支持如下功能：
 *
 * - 更新当前列的视图
 * - 请求`PUT /api/ox/columns/:identifier/my`
 * - 计算影响路径中的Session数据，键值`dataKey`
 *
 * ### 2. 通道详细
 *
 * - 类型：ADAPTOR
 * - 直接从`DataAtom`中读取模型标识符
 * - 内置调用了ServiceLoader通道`io.horizon.spi.ui.ApeakMy`
 *
 * ### 3. 请求格式
 *
 * ```json
 * // <pre><code class="json">
 * [
 *      "column1",
 *      "column2",
 *      "...."
 * ]
 * // </code></pre>
 * ```
 *
 * ### 4. 响应格式
 *
 * ```json
 * // <pre><code class="json">
 * {
 *      "data": [
 *          "column1",
 *          "column2",
 *          "...."
 *      ]
 * }
 * // </code></pre>
 * ```
 *
 * 内置调用{@link Ox#viewMy Ox.viewMy}
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ViewComponent extends AbstractAdaptor {
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

        /* 前置一致，计算 dataKey 的信息，读取我所在的列信息 */
        return Ox.viewMy(envelop, atom.identifier()).compose(params -> {
            final String literal = params.getString(KName.VIEW);
            final String sessionKey = Ke.keyView(params.getString(KName.METHOD),
                params.getString(KName.URI), Vis.create(literal));
            params.put(KName.DATA_KEY, sessionKey);
            return Ux.channelA(ApeakMy.class,
                () -> ActOut.future(new JsonObject()),
                stub -> {
                    /*
                     * 使用 query 参数替代body，此处可以拿到 projection 和 criteria （查询引擎参数）
                     * 此处格式如：
                     * {
                     *      "criteria": {},
                     *      "projection": []
                     * }
                     */
                    final JsonObject requestData = request.getJObject();
                    final JsonObject viewData = requestData.getJsonObject("viewData", new JsonObject());
                    return stub.saveMy(params, viewData)
                        .compose(ActOut::future);
                });
        });
    }
}
