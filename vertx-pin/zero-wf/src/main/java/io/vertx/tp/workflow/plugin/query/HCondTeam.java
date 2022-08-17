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
public class HCondTeam implements HCond {
    @Override
    public Future<JsonObject> compile(final JsonObject data, final JsonObject qr) {
        return Ux.futureJ();
    }
}
