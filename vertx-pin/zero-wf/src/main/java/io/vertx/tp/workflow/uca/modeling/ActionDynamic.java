package io.vertx.tp.workflow.uca.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.feature.Atom;
import io.vertx.tp.workflow.atom.MetaInstance;
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
            Ke.channel(Atom.class, JsonObject::new,
                // Channel
                atom -> atom.createAsync(identifier, data))).apply(params);
    }

    @Override
    public <T> Future<JsonObject> updateAsync(final String key, final JsonObject params, final MetaInstance metadata) {
        return this.identifierJ((identifier, data) ->
            // Single Update
            Ke.channel(Atom.class, JsonObject::new,
                // Channel
                atom -> atom.updateAsync(identifier, key, data))).apply(params);
    }

    @Override
    public <T> Future<JsonObject> fetchAsync(final String key, final String identifier, final MetaInstance metadata) {
        // Single Fetch
        return Ke.channel(Atom.class, JsonObject::new,
            // Channel
            atom -> atom.fetchAsync(identifier, key));
    }

    @Override
    public <T> Future<JsonArray> updateAsync(final Set<String> keys, final JsonArray params, final MetaInstance metadata) {
        // Batch Update
        return this.identifierA((identifier, dataArray) ->
            Ke.channel(Atom.class, JsonArray::new,
                // Channel
                atom -> atom.updateAsync(identifier, keys, dataArray))).apply(params);
    }

    @Override
    public <T> Future<JsonArray> fetchAsync(final Set<String> keys, final String identifier, final MetaInstance metadata) {
        // Batch Fetch
        return Ke.channel(Atom.class, JsonArray::new,
            // Channel
            atom -> atom.fetchAsync(identifier, keys));
    }

    private Function<JsonArray, Future<JsonArray>> identifierA(
        final BiFunction<String, JsonArray, Future<JsonArray>> executor) {
        return recordArray -> {
            final String identifier = Ut.mapOneS(recordArray, KName.IDENTIFIER);
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
