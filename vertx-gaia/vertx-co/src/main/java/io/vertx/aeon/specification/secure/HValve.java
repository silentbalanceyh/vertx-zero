package io.vertx.aeon.specification.secure;

import io.vertx.aeon.runtime.H1H;
import io.vertx.aeon.specification.action.HEvent;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;

/**
 * 「阀」
 * 同时兼容单点系统和分布式的标准权限接口
 * 1. 和 Extension 中的 S_PATH 执行桥接和映射
 * 2. 管理界面：不同管理界面读取不同配置
 * 3. 最终影响：根据配置修改视图
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HValve extends HEvent<JsonObject, JsonObject> {
    /*
     * {
     *     "dmComponent": "HDm Interface",
     *     "sigma": "xxx"
     * }
     */
    static HAdmit instanceDm(final JsonObject request) {
        final String sigma = Ut.valueString(request, KName.SIGMA);
        final Class<?> dmCls = Ut.valueCI(request, KName.Component.DM_COMPONENT, HAdmit.class);
        final HAdmit dm = (HAdmit) H1H.CCT_EVENT.pick(() -> Ut.instance(dmCls),
            // <sigma> / <name>
            sigma + Strings.SLASH + dmCls.getName());
        return dm.bind(sigma);
    }

    /*
     * {
     *     "uiComponent": "HDm Interface",
     *     "sigma": "xxx"
     * }
     */
    static HAdmit instanceUi(final JsonObject request) {
        final String sigma = Ut.valueString(request, KName.SIGMA);
        final Class<?> uiCls = Ut.valueCI(request, KName.Component.UI_COMPONENT, HAdmit.class);
        final HAdmit ui = (HAdmit) H1H.CCT_EVENT.pick(() -> Ut.instance(uiCls),
            // <sigma> / <name>
            sigma + Strings.SLASH + uiCls.getName());
        return ui.bind(sigma);
    }

    /*
     * 根据输入的 JsonObject 提取 HPermit 辅助管理
     * 输入数据格式：
     * {
     *     "name": "xxx",
     *     "code": "xxx",
     *     "mapping": {},
     *
     *     "dmType": "Dm Type",
     *     "dmComponent": "HDm Interface"
     *     "dmCondition": {},
     *     "dmConfig": {},
     *
     *     "uiType": "Ui Type",
     *     "uiComponent": "HUi Interface",
     *     "uiCondition": {},
     *     "uiConfig": {},
     *     "uiSurface": {}
     * }
     */
    @Override
    default Future<JsonObject> configure(final JsonObject input) {
        return HEvent.super.configure(input);
    }
}
