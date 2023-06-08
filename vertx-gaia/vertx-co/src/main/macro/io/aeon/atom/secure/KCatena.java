package io.aeon.atom.secure;

import io.aeon.runtime.CRunning;
import io.horizon.eon.VString;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.specification.secure.HAdmit;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/*
 * 新数据结构，为迎合多种模式量身打造，构造
 * DM / UI 时统一参数用
 * Catena: 连锁（事件），彼此连接的一串内容
 * 和 KPermit 不同点在于，KCatena 只保存处理过程中的数据，不保存配置，所以此处
 * - request：代表输入
 * - configDm / dataDm：代表DM处理前和后的结果
 * - configUi / dataUi：代表UI处理前和后的结果
 */
public class KCatena implements Serializable {
    private final JsonObject request = new JsonObject();
    private final JsonObject configDm = new JsonObject();           // 执行过 configure 后的结果
    private final JsonObject dataDm = new JsonObject();             // 最终 compile 后的结果
    private final JsonObject configUi = new JsonObject();           // 执行过 configure 后的结果
    private final JsonObject dataUi = new JsonObject();             // 最终 compile 后的结果

    private KCatena(final JsonObject request) {
        this.request.mergeIn(request, true);
    }

    public static KCatena instance(final JsonObject request) {
        final JsonObject requestJ = Ut.valueJObject(request);
        return new KCatena(requestJ);
    }

    public static KPermit permit(final JsonObject input) {
        final String sigma = Ut.valueString(input, KName.SIGMA);
        final String code = Ut.valueString(input, KName.CODE);
        // 绑定基础信息：code, sigma, name
        final KPermit permit = KPermit.create(code, sigma, Ut.valueString(input, KName.NAME));


        // 维度信息绑定
        final EmSecure.ScDim dmType = Ut.toEnum(() -> Ut.valueString(input, KName.DM_TYPE), EmSecure.ScDim.class, EmSecure.ScDim.NONE);
        permit.shape(dmType).shape(Ut.valueJObject(input, KName.MAPPING)).shape(
            Ut.valueJObject(input, KName.DM_CONDITION),
            Ut.valueJObject(input, KName.DM_CONFIG)
        );


        // 呈现信息绑定
        final EmSecure.ScIn uiType = Ut.toEnum(
            () -> Ut.valueString(input, KName.UI_TYPE), EmSecure.ScIn.class, EmSecure.ScIn.NONE);
        final EmSecure.ActPhase uiPhase = Ut.toEnum(
            () -> Ut.valueString(input, KName.PHASE), EmSecure.ActPhase.class, EmSecure.ActPhase.ERROR);

        permit.ui(uiType, uiPhase).ui(Ut.valueJObject(input, KName.UI_SURFACE)).ui(
            Ut.valueJObject(input, KName.UI_CONDITION),
            Ut.valueJObject(input, KName.UI_CONFIG)
        );

        // 子节点处理
        final JsonArray childrenJ = Ut.valueJArray(input, KName.CHILDREN);
        Ut.itJArray(childrenJ).map(KCatena::permit).forEach(permit::child);
        return permit;
    }

    // ----------- 组件引用提取 -----------
    /* isDM = true
     * {
     *     "dmComponent": "HDm Interface",
     *     "sigma": "xxx"
     * }
     * isDM = false
     * {
     *     "uiComponent": "HUi interface"
     *     "sigma": "xxx"
     * }
     */
    public HAdmit admit(final boolean isDM) {
        final String sigma = Ut.valueString(this.request, KName.SIGMA);
        final Class<?> admitCls;
        if (isDM) {
            admitCls = Ut.valueCI(this.request, KName.Component.DM_COMPONENT, HAdmit.class);
        } else {
            admitCls = Ut.valueCI(this.request, KName.Component.UI_COMPONENT, HAdmit.class);
        }
        if (Objects.isNull(admitCls)) {
            //  Ui / Dm to avoid null dot
            return null;
        }

        final HAdmit dm = (HAdmit) CRunning.CCT_EVENT.pick(() -> Ut.instance(admitCls),
            // <sigma> / <name>
            sigma + VString.SLASH + admitCls.getName());
        return dm.bind(sigma);
    }

    public KPermit permit() {
        final JsonObject requestJ = Ut.valueJObject(this.request);
        // Build Cache Key Based on `sigma / code`
        final String sigma = Ut.valueString(requestJ, KName.SIGMA);
        final String code = Ut.valueString(requestJ, KName.CODE);
        Ut.requireNonNull(sigma, code);
        // Store KAppOld information
        return CRunning.CC_PERMIT.pick(() -> permit(requestJ), sigma + VString.SLASH + code);
    }

    // ----------- 单独取数 -----------

    public JsonObject data(final boolean isDM) {
        return isDM ? this.dataDm : this.dataUi;
    }

    public JsonObject config(final boolean isDM) {
        return isDM ? this.configDm : this.configUi;
    }

    public Future<KCatena> data(final JsonObject data, final boolean isDM) {
        final JsonObject dataJ = Ut.valueJObject(data);
        if (isDM) {
            this.dataDm.mergeIn(dataJ, true);
        } else {
            this.dataUi.mergeIn(dataJ, true);
        }
        return Future.succeededFuture(this);
    }

    public Future<KCatena> config(final JsonObject config, final boolean isDM) {
        final JsonObject configJ = Ut.valueJObject(config);
        if (isDM) {
            this.configDm.mergeIn(configJ, true);
        } else {
            this.configUi.mergeIn(configJ, true);
        }
        return Future.succeededFuture(this);
    }

    // ----------- 流程取数 -----------
    public JsonObject request() {
        return this.request.copy();
    }

    /*
     * 构造 UI 编译数据
     * {
     *     "dm" -> dataDm
     *     "ui" -> configUi
     *     "in" -> request
     * }
     */
    public JsonObject medium() {
        final JsonObject parameters = new JsonObject();
        parameters.put(KName.Rbac.DM, this.dataDm);
        parameters.put(KName.Rbac.UI, this.configUi);
        parameters.put(KName.IN, this.request);
        return parameters;
    }

    /*
     * 流程数据读取的基础骨架，最终响应数据的基础格式处理
     * 基础格式处理如下：
     * {
     *     "group":         "dm -> items，表示维度计算结果",
     *     "config":        {                                   // "ui -> surface，界面呈现专用配置",
     *          // 配置来源于两个方向
     *          // dmJ -> web前缀配置 DM_CONFIG
     *          // uiJ -> web前缀配置 UI_SURFACE
     *          // 常用配置解析
     *          "webAdmit":             "[], 需要超级管理员权限的定义维度",
     *          "webAction":            "[]|{}, 按钮定义，单个或多个",
     *          "webComponent":         "主组件，最终写入根节点 ui 中",
     *          "webBind":              "xx, 当前管理区域绑定的 S_RESOURCE 资源 code",
     *
     *          "webTree":              "xx, 如果数据以树型呈现，提供树相关配置",
     *          "webTag":               "区域定义，当前数据的所分区域定义",
     *          "webView":              "{}, 当前维度操作的视图维度，主要填充 view 和 position参数和安全对接",
     *
     *          "webData": {
     *              "empty":            "true | false, 读取数据为空时是否全填充，或全禁止",
     *              "initializer":      "数据加载脚本",
     *              "requester":        "数据提交脚本"
     *          },
     *          "webUi": {
     *              "...UI部分独立配置，可共享的键如",
     *
     *              "webTree": "",
     *              "webTag":  ""
     *          }
     *     }
     *     "ui":            "ui -> webComponent，主组件配置处理",
     *     "key":           "input -> 输入主键，对应 S_PATH",
     *     "label":         "input -> 输入名称，对应 S_PATH",
     *     "value":         "input -> 输入编码，对应 S_PATH",
     *     "datum":         {
     *          // "S_PATH对象本身数据"
     *     }
     *     "data":          "<子类填充>,基础数据",
     *     "children":      "<子类填充>,带子节点数据",
     * }
     * 构造四层树，重复键需提取：webTree / webTag
     */
    public JsonObject response() {
        final JsonObject normalized = new JsonObject();
        // group                    = dm -> items
        normalized.put(KName.GROUP, this.dataDm.getValue(KName.ITEMS));
        /*
         * configDm + configUi      = surface
         * 读取所有 web 前缀下的配置
         * 维度 + 数据（都读取配置层）
         * --> uiSurface
         * 重新拟定算法
         * uiSurface,   webTree / webTag 提取到 webUi
         */
        final JsonObject uiSurface = Ut.valueJObject(this.configUi, KName.Rbac.SURFACE).copy();
        {
            // 特殊配置直接来自 uiConfig 字段
            final JsonObject configUi = this.configUi.copy();
            final JsonObject webUi = new JsonObject();
            Ut.itStart(configUi, KName.WEB, (value, field) -> webUi.put(field, value));
            uiSurface.put(KName.Rbac.WEB_UI, webUi);
        }
        final JsonObject configDm = this.configDm.copy();
        Ut.itStart(configDm, KName.WEB, (value, field) -> uiSurface.put(field, value));
        normalized.put(KName.CONFIG, uiSurface);

        /*
         * page
         * webComponent     ->      ui
         * key              ->      key
         * name             ->      label
         * code             ->      value
         * datum            ->      {} store
         */
        normalized.put(KName.Rbac.UI, Ut.valueString(uiSurface, KName.Component.WEB_COMPONENT));
        final JsonObject input = this.request.copy();
        normalized.put(KName.KEY, input.getValue(KName.KEY));
        normalized.put(KName.LABEL, input.getValue(KName.NAME));
        normalized.put(KName.VALUE, input.getValue(KName.CODE));
        normalized.put(KName.DATUM, input.copy());
        return normalized;
    }
}
