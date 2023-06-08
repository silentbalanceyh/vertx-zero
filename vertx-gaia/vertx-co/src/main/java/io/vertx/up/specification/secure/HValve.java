package io.vertx.up.specification.secure;

import io.aeon.atom.secure.KCatena;
import io.horizon.specification.typed.TEvent;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;

/**
 * 「阀」
 * 同时兼容单点系统和分布式的标准权限接口
 * 1. 和 Extension 中的 S_PATH 执行桥接和映射
 * 2. 管理界面：不同管理界面读取不同配置
 * 3. 最终影响：根据配置修改视图
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HValve extends TEvent<JsonObject, JsonObject> {

    static JsonObject output(final KCatena catena) {
        final JsonObject response = catena.response();
        // ui -> surface, data
        final JsonObject uiJ = catena.data(false);
        // data, children
        response.put(KName.DATA, uiJ.getValue(KName.DATA));
        response.put(KName.CHILDREN, uiJ.getValue(KName.CHILDREN));
        return response;
    }

    /*
     * 根据输入的 JsonObject 提取 KPermit 辅助管理
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
        return TEvent.super.configure(input);
    }
}
