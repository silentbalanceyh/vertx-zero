package io.vertx.tp.workflow.plugin.query;

import io.vertx.aeon.specification.query.HCond;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/**
 * 本组专用查询组件
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HQrStandard implements HCond {
    /*
     * openGroup 包含本组
     * acceptedGroup 中可匹配本组
     */
    @Override
    public Future<JsonObject> compile(final JsonObject data, final JsonObject qr) {
        return Ux.futureJ();
    }
}
