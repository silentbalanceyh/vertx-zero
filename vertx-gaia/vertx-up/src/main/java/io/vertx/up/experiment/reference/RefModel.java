package io.vertx.up.experiment.reference;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.exception.web._501JooqReferenceException;
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
class RefModel implements Ref {

    private final transient Class<?> entityT;
    private transient UxJooq jooq;

    public RefModel(final Class<?> entityT) {
        this.entityT = entityT;
    }

    @Override
    public Ref on(final UxJooq jooq) {
        this.jooq = jooq;
        return this;
    }

    /*
     *
     */
    @Override
    public Future<JsonObject> fetchJ(final JsonObject filters) {
        return this.execute(filters, (condition) -> {
            condition.put("", Boolean.TRUE);
            condition.put(KName.SIGMA, filters.getString(KName.SIGMA));
            return this.jooq.fetchOneAsync(condition)
                .compose(Ux::futureJ);
        });
    }

    @Override
    public Future<JsonArray> fetchA(final Set<String> keys) {
        return this.execute(() -> {
            final JsonObject condition = new JsonObject();
            condition.put(KName.MODEL_KEY + ",i", Ut.toJArray(keys));
            return this.jooq.fetchInAsync(KName.MODEL_KEY, Ut.toJArray(keys))
                .compose(Ux::futureA);
        });
    }

    @Override
    public Future<JsonObject> updateJ(final String key, final JsonObject params) {
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
            nexusData.put(KName.MODEL_ID, json.getString(KName.IDENTIFIER));
            nexusData.put(KName.MODEL_KEY, json.getString(KName.KEY));
            return nexusData;
        }
    }
}
