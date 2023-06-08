package io.vertx.mod.workflow.uca.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class ActionDao implements ActionOn {
    @Override
    public <T> Future<JsonObject> createAsync(final JsonObject params, final MetaInstance metadata) {
        final UxJooq jooq = metadata.recordDao();
        Ut.valueToString(params, KName.METADATA);
        return jooq.insertJAsync(params).compose(Fn.ofJObject(KName.METADATA))
            // Normalize Data
            .compose(record -> Ux.futureN(params, null, record));
    }

    @Override
    @SuppressWarnings("all")
    public <T> Future<JsonObject> updateAsync(final String key, final JsonObject params, final MetaInstance metadata) {
        final UxJooq jooq = metadata.recordDao();
        return jooq.<T>fetchByIdAsync(key).compose(query -> {
            // Fix Bug: Cannot deserialize value of type `java.lang.String` from Object value (token `JsonToken.START_OBJECT`)
            final JsonObject original = Ux.toJson(query);
            Ut.valueToString(params, KName.METADATA);
            final T entity = Ux.updateT(query, params);
            return jooq.updateAsync(entity).compose(Ux::futureJ).compose(Fn.ofJObject(KName.METADATA))
                // Normalize Data
                .compose(record -> Ux.futureN(params, original, record));
        });
    }

    @Override
    public <T> Future<JsonObject> fetchAsync(final String key, final String identifier, final MetaInstance metadata) {
        final UxJooq jooq = metadata.recordDao();
        return jooq.fetchByIdAsync(key)
            .compose(Ux::futureJ)
            .compose(Fn.ofJObject(KName.METADATA));
    }

    @Override
    public <T> Future<JsonArray> createAsync(JsonArray params, MetaInstance metadata) {
        final UxJooq jooq = metadata.recordDao();
        Ut.itJArray(params).forEach(json -> Ut.valueToString(json, KName.METADATA));
        return jooq.insertJAsync(params).compose(Fn.ofJArray(KName.METADATA))
            // Normalize Data
            .compose(records -> Ux.futureN(null, records));
    }

    @Override
    public <T> Future<JsonArray> updateAsync(final Set<String> keys, final JsonArray params, final MetaInstance metadata) {
        final UxJooq jooq = metadata.recordDao();
        final JsonObject condition = new JsonObject();
        condition.put(KName.KEY + ",i", Ut.toJArray(keys));
        return jooq.<T>fetchAsync(condition).compose(query -> {
            final JsonArray original = Ux.toJson(query);
            final List<T> updated = Ux.updateT(query, params);
            return jooq.<T>updateAsync(updated).compose(Ux::futureA).compose(Fn.ofJArray(KName.METADATA))
                // Normalize Data
                .compose(records -> Ux.futureN(original, records));
        });
    }

    @Override
    public <T> Future<JsonArray> fetchAsync(final Set<String> keys, final String identifier, final MetaInstance metadata) {
        final UxJooq jooq = metadata.recordDao();
        final JsonObject condition = new JsonObject();
        condition.put(KName.KEY + ",i", Ut.toJArray(keys));
        return jooq.<T>fetchAsync(condition)
            .compose(Ux::futureA)
            .compose(Fn.ofJArray(KName.METADATA));
    }
}
