package io.vertx.tp.workflow.uca.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ActionDao implements ActionOn {
    @Override
    public <T> Future<JsonObject> createAsync(final JsonObject params, final ConfigTodo config) {
        Objects.requireNonNull(config.dao());
        final UxJooq jooq = Ux.Jooq.on(config.dao());
        return null;
    }

    @Override
    @SuppressWarnings("all")
    public <T> Future<JsonObject> updateAsync(final String key, final JsonObject params, final ConfigTodo config) {
        Objects.requireNonNull(config.dao());
        final UxJooq jooq = Ux.Jooq.on(config.dao());
        return jooq.fetchByIdAsync(key).compose(query -> {
            Objects.requireNonNull(query);
            final Class<?> entityCls = query.getClass();
            final JsonObject original = Ux.toJson(query);
            original.mergeIn(params, true);
            final T entity = (T) Ux.fromJson(original, entityCls);
            return jooq.updateAsync(entity);
        }).compose(Ux::futureJ);
    }
}
