package io.vertx.up.uca.jooq;

import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class JoinWriter {
    private final transient JoinStore store;

    JoinWriter(final JoinStore store) {
        this.store = store;
    }

    Future<Boolean> deleteById(final String key) {
        final UxJooq jooq = this.store.jooq();
        return jooq.fetchJByIdAsync(key)
            .compose(response -> this.deleteChild(response)
                .compose(nil -> jooq.deleteByIdAsync(key)));
    }

    Future<JsonObject> insert(final JsonObject data, final String field) {
        final UxJooq jooq = this.store.jooq();
        return jooq.insertJAsync(data).compose(response -> {
            // Joined Data
            final JsonArray record = this.valueNorm(data, field);
            return this.upsertChild(response, record).compose(items -> {
                response.put(field, items);
                return Ux.future(response);
            });
        });
    }

    Future<JsonObject> update(final String key, final JsonObject data, final String field) {
        final UxJooq jooq = this.store.jooq();
        return jooq.updateJAsync(key, data).compose(response -> {
            // Joined Data
            final JsonArray record = this.valueNorm(data, field);
            return this.upsertChild(response, record).compose(items -> {
                response.put(field, items);
                return Ux.future(response);
            });
        });
    }

    private Future<Boolean> deleteChild(final JsonObject response) {
        final UxJooq childJq = this.store.childJooq();
        if (Objects.nonNull(childJq)) {
            final JsonObject joined = this.store.dataJoin(response);
            return childJq.deleteByAsync(joined).compose(nil -> Ux.futureT());
        } else {
            return Ux.future(Boolean.FALSE);
        }
    }

    private Future<JsonArray> upsertChild(final JsonObject response, final JsonArray current) {
        final UxJooq childJq = this.store.childJooq();
        if (Objects.nonNull(childJq)) {
            final JsonObject joined = this.store.dataJoin(response);
            // Compared
            return childJq.fetchJAsync(joined).compose(original -> {
                final JsonObject child = this.valueNorm(response, joined);
                Ut.itJArray(current).forEach(each -> each.mergeIn(child));
                final ConcurrentMap<ChangeFlag, JsonArray> compared = Ux.compareJ(original, current, this.store.childKeySet());
                final List<Future<JsonArray>> futures = new ArrayList<>();
                futures.add(childJq.insertJAsync(compared.get(ChangeFlag.ADD)));
                futures.add(childJq.updateAsync(compared.get(ChangeFlag.UPDATE)).compose(Ux::futureA));
                futures.add(childJq.deleteJAsync(compared.get(ChangeFlag.DELETE)));
                return Fn.compressA(futures);
            });
        } else {
            return Ux.futureA();
        }
    }

    private JsonArray valueNorm(final JsonObject data, final String field) {
        final Object childValue = data.getValue(field);
        final JsonArray dataArray = new JsonArray();
        if (Objects.nonNull(childValue)) {
            if (childValue instanceof JsonObject) {
                dataArray.add(childValue);
            } else if (childValue instanceof JsonArray) {
                dataArray.addAll((JsonArray) childValue);
            }
        }
        return dataArray;
    }

    private JsonObject valueNorm(final JsonObject response, final JsonObject joined) {
        return Ut.valueCopy(joined, response,
            // Normalized
            KName.CREATED_BY,
            KName.CREATED_AT,
            KName.UPDATED_BY,
            KName.UPDATED_AT,
            KName.SIGMA,
            KName.LANGUAGE,
            KName.ACTIVE
        );
    }
}
