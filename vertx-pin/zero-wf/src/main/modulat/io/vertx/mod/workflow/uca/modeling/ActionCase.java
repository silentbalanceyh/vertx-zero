package io.vertx.mod.workflow.uca.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ActionCase implements ActionOn {
    @Override
    public <T> Future<JsonObject> createAsync(final JsonObject params, final MetaInstance metadata) {
        return null;
    }

    @Override
    public <T> Future<JsonObject> updateAsync(final String key, final JsonObject params, final MetaInstance metadata) {
        return null;
    }

    @Override
    public <T> Future<JsonObject> fetchAsync(final String key, final String identifier, final MetaInstance metadata) {
        return null;
    }

    @Override
    public <T> Future<JsonArray> createAsync(final JsonArray params, final MetaInstance instance) {
        return null;
    }

    @Override
    public <T> Future<JsonArray> updateAsync(final Set<String> keys, final JsonArray params, final MetaInstance metadata) {
        return null;
    }

    @Override
    public <T> Future<JsonArray> fetchAsync(final Set<String> keys, final String identifier, final MetaInstance metadata) {
        return null;
    }
}
