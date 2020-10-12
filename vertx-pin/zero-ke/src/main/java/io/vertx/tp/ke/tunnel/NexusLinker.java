package io.vertx.tp.ke.tunnel;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._501JooqReferenceException;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/*
 * Spec Linker for `modelKey` and `modelId`
 * Usage
 */
class NexusLinker implements Nexus {

    private final transient Class<?> entityT;
    private transient UxJooq jooq;

    public NexusLinker(final Class<?> entityT) {
        this.entityT = entityT;
    }

    @Override
    public Nexus on(final UxJooq jooq) {
        this.jooq = jooq;
        return this;
    }

    /*
     *
     */
    @Override
    public Future<JsonObject> fetchNexus(final JsonObject filters) {
        return this.execute(filters, (condition) -> {
            condition.put("", Boolean.TRUE);
            condition.put(KeField.SIGMA, filters.getString(KeField.SIGMA));
            return this.jooq.fetchOneAsync(condition)
                    .compose(Ux::futureJ);
        });
    }

    @Override
    public Future<JsonArray> fetchNexus(final Set<String> keys) {
        return this.execute(() -> {
            final JsonObject condition = new JsonObject();
            condition.put(KeField.MODEL_KEY + ",i", Ut.toJArray(keys));
            return this.jooq.fetchInAsync(KeField.MODEL_KEY, Ut.toJArray(keys))
                    .compose(Ux::futureA);
        });
    }

    @Override
    public Future<JsonObject> updateNexus(final String key, final JsonObject params) {
        return this.execute(params, (updatedData) -> this.jooq.fetchByIdAsync(key)
                .compose(Ux::futureJ)
                .compose(original -> {
                    original.mergeIn(updatedData);
                    final Object entity = Ut.deserialize(original, this.entityT);
                    return this.jooq.updateAsync(entity)
                            .compose(Ux::futureJ);
                })
        );
    }

    private <T> Future<T> execute(final JsonObject params, final Function<JsonObject, Future<T>> future) {
        if (Objects.isNull(this.jooq)) {
            return Future.failedFuture(new _501JooqReferenceException(this.getClass()));
        } else {
            final JsonObject data = this.getData(params);
            if (Objects.isNull(data)) {
                return Future.failedFuture(new _501JooqReferenceException(this.getClass()));
            } else {
                return future.apply(data);
            }
        }
    }

    private <T> Future<T> execute(final Supplier<Future<T>> future) {
        if (Objects.isNull(this.jooq)) {
            return Future.failedFuture(new _501JooqReferenceException(this.getClass()));
        } else {
            return future.get();
        }
    }

    private JsonObject getData(final JsonObject json) {
        if (Ut.isNil(json)) {
            return null;
        } else {
            final JsonObject nexusData = new JsonObject();
            nexusData.put(KeField.MODEL_ID, json.getString(KeField.IDENTIFIER));
            nexusData.put(KeField.MODEL_KEY, json.getString(KeField.KEY));
            return nexusData;
        }
    }
}
