package io.vertx.mod.workflow.uca.modeling;

import io.horizon.spi.modeler.Atom;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ActionDynamic implements ActionOn {
    @Override
    public <T> Future<JsonObject> createAsync(final JsonObject params, final MetaInstance metadata) {
        return this.identifierJ((identifier, data) ->
            // Single Create
            Ux.channel(Atom.class, JsonObject::new,
                // Channel
                atom -> atom.createAsync(identifier, data))).apply(params);
    }

    @Override
    public <T> Future<JsonObject> updateAsync(final String key, final JsonObject params, final MetaInstance metadata) {
        return this.identifierJ((identifier, data) ->
            // Single Update
            Ux.channel(Atom.class, JsonObject::new,
                // Channel
                atom -> atom.updateAsync(identifier, key, data))).apply(params);
    }

    @Override
    public <T> Future<JsonObject> fetchAsync(final String key, final String identifier, final MetaInstance metadata) {
        // Single Fetch
        return Ux.channel(Atom.class, JsonObject::new,
            // Channel
            atom -> atom.fetchAsync(identifier, key));
    }

    @Override
    public <T> Future<JsonArray> createAsync(final JsonArray params, final MetaInstance instance) {
        return this.identifierA((identifier, dataArray) ->
            // Batch Create
            Ux.channel(Atom.class, JsonArray::new,
                // Channel
                atom -> atom.createAsync(identifier, dataArray))).apply(params);
    }

    @Override
    public <T> Future<JsonArray> updateAsync(final Set<String> keys, final JsonArray params, final MetaInstance metadata) {
        // Batch Update
        return this.identifierA((identifier, dataArray) ->
            Ux.channel(Atom.class, JsonArray::new,
                // Channel
                atom -> atom.updateAsync(identifier, keys, dataArray))).apply(params);
    }

    @Override
    public <T> Future<JsonArray> fetchAsync(final Set<String> keys, final String identifier, final MetaInstance metadata) {
        // Batch Fetch
        return Ux.channel(Atom.class, JsonArray::new,
            // Channel
            atom -> atom.fetchAsync(identifier, keys));
    }

    private Function<JsonArray, Future<JsonArray>> identifierA(
        final BiFunction<String, JsonArray, Future<JsonArray>> executor) {
        return recordArray -> {
            final String identifier = Ut.valueString(recordArray, KName.IDENTIFIER);
            if (Ut.isNil(identifier)) {
                return Ux.futureA();
            } else {
                return executor.apply(identifier, recordArray);
            }
        };
    }

    private Function<JsonObject, Future<JsonObject>> identifierJ(
        final BiFunction<String, JsonObject, Future<JsonObject>> executor) {
        return recordData -> {
            final String identifier = recordData.getString(KName.IDENTIFIER, null);
            if (Ut.isNil(identifier)) {
                return Ux.futureJ();
            } else {
                return executor.apply(identifier, recordData);
            }
        };
    }
}
