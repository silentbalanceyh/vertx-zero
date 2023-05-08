package io.modello.atom.app;

import io.horizon.eon.VName;
import io.horizon.eon.VString;
import io.horizon.runtime.cache.CStore;
import io.horizon.util.HUt;
import io.modello.util.HMs;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KApp implements Serializable {

    // -------------- 业务级数据 -----------------
    private final String name;      // vie.app.xxxx
    // language / sigma     通用条件
    private final JsonObject dimJ = new JsonObject();
    // name / namespace     业务专用条件
    private final JsonObject dimB = new JsonObject();
    // appId / appKey       系统专用条件
    private final JsonObject dimA = new JsonObject();
    private String ns;              // ( namespace )
    // -------------- 标识 / 语言维度 -----------------
    private String language;        // X-Lang
    private String sigma;           // X-Sigma
    // -------------- 系统级数据 ---------------------
    private String appId;          // X-App-Id
    private String appKey;         // X-App-Key
    private String code;           // code

    private KDatabase database;     // 数据库专用引用

    // -------------- 实例化应用 ----------------------
    private KApp(final String name) {
        this.name = name;
        this.ns = HMs.nsApp(name); // HApp.ns(name);
    }

    // JtJob 专用实例化应用的新方法
    public static KApp instance() {
        return new KApp(null);
    }

    public static KApp instance(final String name) {
        return new KApp(name);
    }

    public KApp bind(final JsonObject unityApp) {
        /* appId, appKey, code */
        final JsonObject appJ = HUt.valueJObject(unityApp);
        this.appId = HUt.valueString(appJ, VName.APP_ID);
        this.appKey = HUt.valueString(appJ, VName.APP_KEY);
        this.code = HUt.valueString(appJ, VName.CODE);

        /* sigma / language */
        this.sigma = HUt.valueString(appJ, VName.SIGMA);
        this.language = HUt.valueString(appJ, VName.LANGUAGE);

        {
            this.dimJ.put(VName.SIGMA, this.sigma);
            this.dimJ.put(VName.LANGUAGE, this.language);

            this.dimA.put(VName.APP_ID, this.appId);
            this.dimA.put(VName.APP_KEY, this.appKey);

            this.dimB.put(VName.NAME, this.name);
            this.dimB.put(VName.NAMESPACE, this.ns);
        }

        /* database */
        final JsonObject sourceJ = HUt.valueJObject(appJ, VName.SOURCE);
        if (HUt.isNotNil(sourceJ)) {
            this.database = new KDatabase();
            this.database.fromJson(sourceJ);
        }

        /* Sync with Cache here */
        return this;
    }

    public KApp bind(final String namespace) {
        /*
         * Sync with Cache when the namespace has been changed
         * If namespace changed, here should refresh the POOL `H3H.CC_APP` cache
         * Fix Issue: https://e.gitee.com/szzw/dashboard?issue=I5P1Y1
         * */
        Objects.requireNonNull(namespace);
        if (!namespace.equals(this.ns)) {
            final ConcurrentMap<String, KApp> store = CStore.CC_APP.store();
            //            store.clear(this.ns);
            store.remove(this.ns);
            this.ns = namespace;
            // synchro() to replace the whole cache data
            this.synchro();
        }
        return this;
    }

    public KApp bind(final String sigma, final String language) {
        this.sigma = sigma;
        this.language = language;
        return this;
    }

    /* 必须是完整同步才执行填充 */
    public KApp synchro() {
        final Map<String, KApp> store = CStore.CC_APP.store();
        store.put(this.ns, this);
        store.put(this.appKey, this);
        store.put(this.appId, this);
        store.put(this.code, this);
        store.put(this.sigma, this);
        // Old Code is as following
        // Fix Issue: https://e.gitee.com/szzw/dashboard?issue=I5P1Y1
        //        H3H.CC_APP.pick(() -> this, this.ns);
        //        H3H.CC_APP.pick(() -> this, this.appKey);
        //        H3H.CC_APP.pick(() -> this, this.appId);
        //        H3H.CC_APP.pick(() -> this, this.code);
        //        H3H.CC_APP.pick(() -> this, this.sigma);
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

    @SuppressWarnings("unchecked")
    public <T extends KDatabase> T database() {
        return (T) this.database;
    }

    public JsonObject dimJ() {
        return this.dimJ;
    }

    public JsonObject dimA() {
        return this.dimA;
    }

    public JsonObject dimB() {
        return this.dimB;
    }

    /*
     * 参数：
     * {
     *     "appId": "xx",
     *     "appKey": "xx",
     *     "sigma": "xx",
     *     "language": "xx",
     *     "name": "xx",
     *     "ns": "xx"
     * }
     */
    public JsonObject dataJ() {
        final JsonObject parameters = new JsonObject();
        parameters.put(VName.APP_KEY, this.appKey);
        parameters.put(VName.APP_ID, this.appId);
        parameters.put(VName.LANGUAGE, this.language);
        parameters.put(VName.SIGMA, this.sigma);
        parameters.put(VName.NAME, this.name);
        parameters.put(VName.NAMESPACE, this.ns);
        return parameters;
    }

    // -------------- 应用相关的统一标识符 --------------
    /*
     * 主要用于模型的唯一键，最终结构如：
     * <namespace>/<identifier>
     */
    public String keyUnique(final String identifier) {
        return this.ns + VString.DASH + identifier;
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
