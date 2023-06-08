package io.mature.extension.quiz.atom;

import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * ## 「Pojo」测试输入请求
 *
 * ### 1. 基本介绍
 *
 * 构造Ox平台测试输入请求对象，它提供如下功能
 *
 * - 构造统一请求传输模型对象{@link Envelop}（Zero专用）。
 * - 构造请求头封装对象{@link MultiMap}。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QRequest implements Serializable {
    /**
     * {@link String} <strong>模拟</strong>请求唯一标识
     */
    private transient final String requestKey;
    /**
     * {@link Envelop} 请求传输模型对象
     */
    private transient final Envelop envelop;
    /**
     * {@link MultiMap} 请求头封装对象
     */
    private transient final MultiMap headers = MultiMap.caseInsensitiveMultiMap();

    /**
     * 根据Json数据构造`QRequest`请求对象
     *
     * 输入数据结构如下：
     *
     * ```json
     * // <pre><code class="json">
     *     {
     *          "request": "请求唯一标识",
     *          "data": "支持两种格式，[]和{}，对应JsonObject和JsonArray",
     *          "headers": {
     *              "...": "请求头哈希表结构，存储appId, appKey, sigma等..."
     *          }
     *     }
     * // </code></pre>
     * ```
     *
     * @param data 输入的{@link JsonObject}数据对象。
     */
    public QRequest(final JsonObject data) {
        this.requestKey = data.getString("request");
        this.envelop = Envelop.success(data.getValue("data"));
        if (data.containsKey("headers")) {
            Ut.<String>itJObject(data.getJsonObject("headers"), (value, key) ->
                this.headers.add(key, value));
        }
    }

    /**
     * 返回当前请求的唯一标识
     *
     * @return {@link String}
     */
    public String key() {
        return this.requestKey;
    }

    /**
     * 返回当前请求内部的请求传输模型对象
     *
     * @return {@link Envelop}
     */
    public Envelop envelop() {
        return this.envelop;
    }

    /**
     * 根据`I_API/I_SERVICE`定义的接口合并对象和应用配置合并对象填充当前`QRequest`对象并设置其内部数据。
     *
     * @param uri {@link JtUri} 合并`I_API/I_SERVICE`中的合并定义对象，存储了接口的定义数据
     * @param ark {@link io.macrocosm.specification.program.HArk} 构造的当前应用的配置数据信息，包括内部绑定的数据源`Database`
     */
    public void mount(final JtUri uri, final HArk ark) {
        /* XHeaders */
        final HApp app = ark.app();
        // 1. X-App-Id
        final String appId = app.option(KName.APP_ID);
        if (!this.headers.contains(KWeb.HEADER.X_APP_ID)) {
            this.headers.add(KWeb.HEADER.X_APP_ID, appId);
        }
        // 2. X-App-Key
        final String appKey = app.option(KName.APP_KEY);
        if (!this.headers.contains(KWeb.HEADER.X_APP_KEY)) {
            this.headers.add(KWeb.HEADER.X_APP_KEY, appKey);
        }
        // 3. X-Lang
        final String language = app.option(KName.LANGUAGE);
        if (!this.headers.contains(KWeb.HEADER.X_LANG)) {
            this.headers.add(KWeb.HEADER.X_LANG, language);
        }
        // 4. X-Sigma
        final String sigma = app.option(KName.SIGMA);
        if (!this.headers.contains(KWeb.HEADER.X_SIGMA)) {
            this.headers.add(KWeb.HEADER.X_SIGMA, sigma);
        }
        /* Process Web */
        this.envelop.headers(this.headers);
        this.envelop.method(uri.method());
        this.envelop.uri(uri.path());
    }
}
