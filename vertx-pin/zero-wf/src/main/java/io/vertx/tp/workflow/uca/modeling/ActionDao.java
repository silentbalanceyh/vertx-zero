package io.vertx.tp.workflow.uca.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.up.eon.KName;
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
        Ut.ifString(params, KName.METADATA);
        return jooq.insertJAsync(params).compose(Ut.ifJObject(KName.METADATA));
    }

    @Override
    @SuppressWarnings("all")
    public <T> Future<JsonObject> updateAsync(final String key, final JsonObject params, final MetaInstance metadata) {
        final UxJooq jooq = metadata.recordDao();
        return jooq.<T>fetchByIdAsync(key).compose(query -> {
            // Fix Bug: Cannot deserialize value of type `java.lang.String` from Object value (token `JsonToken.START_OBJECT`)
            Ut.ifString(params, KName.METADATA);
            final T entity = Ux.updateT(query, params);
            return jooq.updateAsync(entity);
        }).compose(Ux::futureJ).compose(Ut.ifJObject(KName.METADATA));
    }

    @Override
    public <T> Future<JsonObject> fetchAsync(final String key, final String identifier, final MetaInstance metadata) {
        final UxJooq jooq = metadata.recordDao();
        return jooq.fetchByIdAsync(key)
            .compose(Ux::futureJ)
            .compose(Ut.ifJObject(KName.METADATA));
    }

    @Override
    public <T> Future<JsonArray> updateAsync(final Set<String> keys, final JsonArray params, final MetaInstance metadata) {
        final UxJooq jooq = metadata.recordDao();
        final JsonObject condition = new JsonObject();
        condition.put(KName.KEY + ",i", Ut.toJArray(keys));
        return jooq.<T>fetchAsync(condition).compose(query -> {
            final List<T> updated = Ux.updateT(query, params);
            return jooq.<T>updateAsync(updated)
                .compose(Ux::futureA)
                .compose(Ut.ifJArray(KName.METADATA));
        });
    }

    @Override
    public <T> Future<JsonArray> fetchAsync(final Set<String> keys, final String identifier, final MetaInstance metadata) {
        final UxJooq jooq = metadata.recordDao();
        final JsonObject condition = new JsonObject();
        condition.put(KName.KEY + ",i", Ut.toJArray(keys));
        return jooq.<T>fetchAsync(condition)
            .compose(Ux::futureA)
            .compose(Ut.ifJArray(KName.METADATA));
    }
}
