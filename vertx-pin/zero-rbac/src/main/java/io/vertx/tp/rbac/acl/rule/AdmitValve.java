package io.vertx.tp.rbac.acl.rule;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.specification.secure.HValve;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * Dim  =  FLAT
 * Ui   =  DAO
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AdmitValve implements HValve {
    /*
     * {
     *     "appId": "应用程序key值，非云端为 X_APP 表结构中的数据",
     *     "tenantId": "云平台专用（如果平台为多租户时才使用）",
     *     "sigma": "统一标识符，为 sigma 完整平台值"
     * }
     */
    @Override
    public Future<HPermit> configure(final JsonObject input) {
        return HValve.super.configure(input);
    }
}
