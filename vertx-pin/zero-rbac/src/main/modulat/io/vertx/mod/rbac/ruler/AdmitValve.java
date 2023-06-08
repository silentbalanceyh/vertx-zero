package io.vertx.mod.rbac.ruler;

import io.aeon.atom.secure.KCatena;
import io.aeon.atom.secure.KPermit;
import io.aeon.experiment.specification.secure.AbstractValve;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.specification.secure.HValve;
import io.vertx.up.unity.Ux;

/**
 * Dim  =  FLAT
 * Ui   =  DAO
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AdmitValve extends AbstractValve {

    /*
     * Input Dim
     * {
     *     "appId": "应用程序key值，非云端为 X_APP 表结构中的数据",
     *     "tenantId": "云平台专用（如果平台为多租户时才使用）",
     *     "sigma": "统一标识符，为 sigma 完整平台值"
     * }
     *
     * 此处方法的输入结构如
     *
     * {
     *     "dimJ": {
     *         "items": [] | {},
     *         "mapping": {},
     *         "qr": {}
     *     },
     *     "uiJ": {
     *         "dao": "xxx",
     *         "output": "xxx",
     *         "qr": {},
     *         "surface": "",
     *         "data": {} / []
     *     },
     *     "input": {
     *         ...
     *     }
     * }
     *
     * 最终输出格式
     *
     * {
     *     "group":  "dm -> items",
     *     "config": "ui -> surface",
     *     "data":   "ui -> data",
     *     "...page":  {
     *          "ui":  "ui -> webComponent",
     *          "key":   "input -> key",
     *          "label": "input -> name",
     *          "value": "input -> code",
     *          "datum":  "input"
     *     }
     * }
     *
     * - dm 的 "mapping" 节点争取在后端直接计算得到相关结果
     * - 其他节点都在后端消费过，没有必要再返回前端
     */

    @Override
    protected Future<JsonObject> response(final KPermit permit, final KCatena catena) {
        return Ux.futureJ(HValve.output(catena));
    }
}
