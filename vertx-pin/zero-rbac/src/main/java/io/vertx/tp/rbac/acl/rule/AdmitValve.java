package io.vertx.tp.rbac.acl.rule;

import io.vertx.aeon.specification.secure.AbstractValve;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

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
    protected Future<JsonObject> output(final JsonObject input, final JsonObject dimJ, final JsonObject uiJ) {
        final JsonObject response = new JsonObject();

        // dm -> items
        final Object dmGroup = dimJ.getValue(KName.ITEMS);
        response.put(KName.GROUP, dmGroup);

        // ui -> surface, data
        final JsonObject uiSurface = Ut.valueJObject(uiJ, KName.Rbac.SURFACE).copy();

        /*
         * All the properties that start with `web` will be copied into `uiSurface`
         * The front-end will be combined into `config` all.
         * Here are the first rule of configuration specification:
         * 1. The uiSurface contains `webX` attributes;
         * 2. The `webX` attribute of `dimJ` will be copied into uiSurface
         */
        dimJ.fieldNames().stream().filter(field -> field.startsWith("web"))
                .forEach(field -> uiSurface.put(field, dimJ.getValue(field)));

        response.put(KName.CONFIG, uiSurface);
        final Object uiData = uiJ.getValue(KName.DATA);
        response.put(KName.DATA, uiData);

        // page
        final JsonObject pageData = new JsonObject();
        pageData.put(KName.Rbac.UI, Ut.valueString(uiSurface, "webComponent"));
        pageData.put(KName.KEY, input.getValue(KName.KEY));
        pageData.put(KName.LABEL, input.getValue(KName.NAME));
        pageData.put(KName.VALUE, input.getValue(KName.CODE));
        pageData.put(KName.DATUM, input.copy());
        response.mergeIn(pageData, true);
        return Ux.futureJ(response);
    }
}
