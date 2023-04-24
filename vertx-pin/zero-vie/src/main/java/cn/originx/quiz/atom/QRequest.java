package cn.originx.quiz.atom;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.tp.jet.atom.JtUri;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.web.ID;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.function.Supplier;

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
     * @param app {@link JtApp} 构造的当前应用的配置数据信息，包括内部绑定的数据源`Database`
     */
    public void mount(final JtUri uri, final JtApp app) {
        /* XHeaders */
        this.mount(ID.Header.X_APP_ID, app::getAppId);
        this.mount(ID.Header.X_APP_KEY, app::getAppKey);
        this.mount(ID.Header.X_LANG, app::getLanguage);
        this.mount(ID.Header.X_SIGMA, app::getSigma);

        /* Process Web */
        this.envelop.headers(this.headers);
        this.envelop.method(uri.method());
        this.envelop.uri(uri.path());
    }

    /**
     * 私有统一方法，用来执行值设置，不存在时才调用`add`方法，防止重复值，{@link MultiMap}类内部
     * 并不是使用的哈希表的格式，为了兼容Http求求中的`@Matrix`参数，内置的key是可重复的，可实现集合操作。
     *
     * @param key           {@link String} 将存储在请求头中的哈希键
     * @param valueSupplier {@link Supplier} Java函数对象
     */
    private void mount(final String key, final Supplier<String> valueSupplier) {
        if (!this.headers.contains(key)) {
            this.headers.add(key, valueSupplier.get());
        }
    }
}
