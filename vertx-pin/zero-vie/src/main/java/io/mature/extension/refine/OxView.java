package io.mature.extension.refine;

import io.horizon.eon.VValue;
import io.horizon.spi.ui.Apeak;
import io.horizon.spi.web.Seeker;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mod.jet.atom.JtConfig;
import io.vertx.mod.jet.init.JtPin;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * ## 视图工具
 *
 * ### 1. 基本介绍
 *
 * 视图专用处理工具，全列执行和我的列执行专用函数。
 *
 * ### 2. 支持功能
 *
 * - 全列视图
 * - 我的列视图
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class OxView {

    /**
     * {@link JtConfig}配置对象
     *
     * 配置格式：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *      "wall": "/api"
     *      "worker":{
     *          "instances": 64
     *      },
     *      "agent":{
     *          "instances": 32
     *      }
     * }
     * // </code></pre>
     * ```
     */
    private static final JtConfig CONFIG = JtPin.getConfig();

    /*
     * 私有构造函数（工具类转换）
     */
    private OxView() {
    }

    /**
     * 我的视图全列工具执行器
     *
     * `GET /api/ox/columns/:identifier/all`全列读取请求
     *
     * 请求数据格式
     *
     * - identifier：模型标识符
     * - dynamic：是否动态视图（存储在`S_VIEW`中）
     * - view：视图类型，默认值`DEFAULT`
     *
     * 除开上述参数后，还包含`Http Header`的所有信息。
     *
     * @param envelop    {@link Envelop} Zero标准请求模型
     * @param identifier {@link String} 模型标识符
     *
     * @return {@link Future}<{@link JsonArray}>
     */
    @SuppressWarnings("all")
    static Future<JsonArray> viewFull(final Envelop envelop, final String identifier) {

        /* 构造Json数据 */
        final JsonObject params = new JsonObject();
        params.put(KName.IDENTIFIER, identifier);
        params.put(KName.DYNAMIC, Boolean.TRUE);
        params.put(KName.VIEW, VValue.DFT.V_VIEW);          // 默认使用 DEFAULT

        /* 构造Header数据 */
        final JsonObject header = envelop.headersX();
        if (Ut.isNotNil(header)) {
            params.mergeIn(header, true);
        }
        /* Apeak found */
        return Ux.channel(Apeak.class, JsonArray::new, apeak -> apeak.fetchFull(params));
    }

    /**
     * 我的视图列工具执行器
     *
     * `GET /api/ox/columns/:identifier/my`我的视图列读取请求
     *
     * 请求数据格式
     *
     * - uri：当前请求路径
     * - method：当前HTTP方法
     * - 合并了所有`Http Header`的内容
     *
     * 返回数据格式：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *     "user": "用户主键",
     *     "habitus": "构造的权限池信息",
     *     "view": "视图类型"
     * }
     * // </code></pre>
     * ```
     *
     * @param envelop    {@link Envelop} Zero标准请求模型
     * @param identifier {@link String} 模型标识符
     *
     * @return {@link Future}<{@link JsonArray}>
     */
    @SuppressWarnings("all")
    static Future<JsonObject> viewMy(final Envelop envelop, final String identifier) {
        /*
         * 1. 读取前缀信息
         * /api/ox
         */
        final HArk ark = Ke.ark(envelop.headers());
        final HApp app = ark.app();
        /* 构造 路径参数 */
        final String route = app.option(KName.App.ROUTE);
        /* 构造 安全路径 */
        final String wall = CONFIG.getWall();
        final String prefix = wall + route;

        /*
         * 2. 构造View 的读取专用参数
         */
        final JsonObject params = new JsonObject();
        params.put(KName.URI, MessageFormat.format(prefix + "/{0}/search", identifier));
        params.put(KName.METHOD, HttpMethod.POST.name());

        final JsonObject headers = envelop.headersX();
        params.mergeIn(headers, true);

        final RoutingContext context = envelop.context();
        final String view = context.request().getParam(KName.VIEW);
        /*
         * 3. Seeker 读取视图
         */
        return Ux.channel(Seeker.class, JsonObject::new, seeker -> seeker.fetchImpact(params).compose(item -> {
            /*
             * 4. 构造参数
             */
            item.put(KName.VIEW, Objects.isNull(view) ? VValue.DFT.V_VIEW : view);
            /*
             * 5. 填充用户数据
             */
            item.put(KName.USER, envelop.userId());
            item.put(KName.HABITUS, envelop.habitus());
            return Ux.future(item);
        }));
    }
}
