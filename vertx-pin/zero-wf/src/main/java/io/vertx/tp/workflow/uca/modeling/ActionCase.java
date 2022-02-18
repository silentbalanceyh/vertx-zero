package io.vertx.tp.workflow.uca.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ActionCase implements ActionOn {
    @Override
    public Future<JsonObject> createAsync(final JsonObject params, final ConfigTodo config) {
        return null;
    }

    @Override
    public Future<JsonObject> updateAsync(final String key, final JsonObject params, final ConfigTodo config) {
        return null;
    }

    @Override
    public Future<JsonObject> fetchAsync(final String key, final ConfigTodo config) {
        return null;
    }
}
