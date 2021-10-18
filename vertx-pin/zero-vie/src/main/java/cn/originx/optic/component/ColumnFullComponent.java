package cn.originx.optic.component;

import cn.originx.refine.Ox;
import cn.originx.scaffold.component.AbstractAdaptor;
import io.vertx.core.Future;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;
import io.vertx.up.commune.Envelop;

/**
 * ## 「Channel」全列读取通道
 *
 * ### 1. 基本介绍
 *
 * 不带任何<strong>集成功能</strong>的标准读取全列组件，支持如下功能：
 *
 * - 根据`identifier`读取模型全属性列数据。
 * - 该属性列构造会映射到前端Ant Design中的`<Table/>`元素的columns属性中。
 * - 请求`GET /api/ox/columns/:identifier/full`
 *
 * ### 2. 通道详细
 *
 * - 类型：ADAPTOR
 * - 直接从`DataAtom`中读取模型标识符
 * - 内置调用`ServiceLoader`通道`io.vertx.tp.optic.Apeak`
 *
 * ### 3. 请求格式
 *
 * 无请求体（Body）数据，直接传入identifier读取。
 *
 * ### 4. 响应格式
 *
 * #### 4.1. 格式一，完整格式
 *
 * ```json
 * // <pre><code class="json">
 * [
 *      {
 *          "title": "列标题",
 *          "dataIndex": "属性名",
 *          "render": "渲染类型",
 *          "...": "其他配置"
 *      }
 * ]
 * // </code></pre>
 * ```
 *
 * #### 4.2. 格式二，待解析格式
 *
 * ```json
 * // <pre><code class="json">
 * {
 *      "data": [
 *          {
 *              "metadata": "属性名,列标题,渲染类型",
 *              "...": "其他配置"
 *          },
 *          "属性名2,列标题2"
 *      ]
 * }
 * // </code></pre>
 * ```
 *
 * > 列格式的详细信息可参考前端文档[http://www.vertxui.cn](http://www.vertxui.cn)。
 *
 * 内置调用{@link Ox#viewFull Ox.viewFull}
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ColumnFullComponent extends AbstractAdaptor {

    /**
     * 「Async」通道主方法
     *
     * @param request 通道的标准请求参数，类型{@link ActIn}。
     *
     * @return 返回`{@link Future}<{@link ActOut}>`
     */
    @Override
    public Future<ActOut> transferAsync(final ActIn request) {
        /* 元数据读取 */
        final DataAtom atom = this.atom();
        final Envelop envelop = request.getEnvelop();

        return Ox.viewFull(envelop, atom.identifier())
            .compose(ActOut::future);
    }
}
