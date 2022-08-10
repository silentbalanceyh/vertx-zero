package io.vertx.tp.rbac.acl.rule;

import io.vertx.aeon.specification.secure.AbstractValve;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
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
     * Output Data
     *
     * {
     *     "dm": {},
     *     "ui": {},
     *     "in": {}
     * }
     */
    @Override
    protected Future<JsonObject> response(final JsonObject input, final JsonObject dimJ, final JsonObject uiJ) {

        return Ux.futureJ();
    }
}
