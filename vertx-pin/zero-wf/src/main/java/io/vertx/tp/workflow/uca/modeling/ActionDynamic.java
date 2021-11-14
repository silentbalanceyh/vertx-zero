package io.vertx.tp.workflow.uca.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ActionDynamic implements ActionOn {
    @Override
    public Future<JsonObject> createAsync(final JsonObject params) {
        return null;
    }

    @Override
    public Future<JsonObject> updateAsync(final String key, final JsonObject params) {
        return null;
    }
}
