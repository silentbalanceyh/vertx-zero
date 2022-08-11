package io.vertx.up.experiment.specification.power;

import io.vertx.aeon.runtime.H3H;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.experiment.mixture.HApp;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KApp implements Serializable {

    // -------------- 业务级数据 -----------------
    private final String name;      // vie.app.xxxx
    private String ns;              // ( namespace )


    // -------------- 标识 / 语言维度 -----------------
    private String language;        // X-Lang
    private String sigma;           // X-Sigma


    // -------------- 系统级数据 ---------------------
    private String appId;          // X-App-Id
    private String appKey;         // X-App-Key
    private String code;           // code

    private Database database;     // 数据库专用引用

    // -------------- 实例化应用 ----------------------
    private KApp(final String name) {
        this.name = name;
        this.ns = HApp.ns(name);
    }

    // JtJob 专用实例化应用的新方法
    public static KApp instance() {
        return new KApp(null);
    }

    public static KApp instance(final String name) {
        return new KApp(name);
    }

    // 从上下文环境中提取 KApp 方法
    public static KApp context(final String name) {
        return H3H.CC_APP.pick(() -> new KApp(name), name);
    }

    public static KApp context(final JsonObject unityApp) {
        final String name = Ut.valueString(unityApp, KName.NAME);
        return H3H.CC_APP.pick(() -> new KApp(name), name).bind(unityApp);
    }

    private KApp bind(final JsonObject unityApp) {
        /* appId, appKey, code */
        final JsonObject appJ = Ut.valueJObject(unityApp);
        this.appId = Ut.valueString(appJ, KName.APP_ID);
        this.appKey = Ut.valueString(appJ, KName.APP_KEY);
        this.code = Ut.valueString(appJ, KName.CODE);

        /* sigma / language */
        this.sigma = Ut.valueString(appJ, KName.SIGMA);
        this.language = Ut.valueString(appJ, KName.LANGUAGE);

        /* database */
        final JsonObject sourceJ = Ut.valueJObject(appJ, KName.SOURCE);
        if (Ut.notNil(sourceJ)) {
            this.database = new Database();
            this.database.fromJson(sourceJ);
        }
        return this;
    }

    public KApp bind(final String namespace) {
        this.ns = namespace;
        return this;
    }

    public KApp bind(final String sigma, final String language) {
        this.sigma = sigma;
        this.language = language;
        return this;
    }

    // -------------- 应用程序核心数据 -----------------
    /*
     * name:         应用程序名称
     * ns:           名空间
     * language:     语言
     * sigma:        统一标识符
     */
    public String name() {
        return this.name;
    }

    public String ns() {
        return this.ns;
    }

    public String language() {
        return this.language;
    }

    public String sigma() {
        return this.sigma;
    }

    // -------------- 应用相关的统一标识符 --------------
    /*
     * 主要用于模型的唯一键，最终结构如：
     * <namespace>/<identifier>
     */
    public String keyUnique(final String identifier) {
        return this.ns + Strings.DASH + identifier;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final KApp kApp = (KApp) o;
        return Objects.equals(this.name, kApp.name) &&
            Objects.equals(this.ns, kApp.ns) &&
            Objects.equals(this.language, kApp.language) &&
            Objects.equals(this.sigma, kApp.sigma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.ns, this.language, this.sigma);
    }

    @Override
    public String toString() {
        return "KApp{" +
            "name='" + this.name + '\'' +
            ", ns='" + this.ns + '\'' +
            ", language='" + this.language + '\'' +
            ", sigma='" + this.sigma + '\'' +
            '}';
    }
}
