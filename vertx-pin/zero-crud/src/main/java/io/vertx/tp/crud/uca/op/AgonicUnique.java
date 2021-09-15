package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.up.eon.Values;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.function.BiFunction;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicUnique implements Agonic {

    protected Future<JsonObject> fetchBy(final JsonObject condition, final IxMod in) {
        Ix.Log.filters(this.getClass(), "( Unique ) By: identifier: {0}, condition: {1}",
            in.module().getIdentifier(), condition);
        final UxJooq jooq = IxPin.jooq(in);
        return jooq.fetchJOneAsync(condition);
    }

    protected Future<JsonObject> fetchByPk(final JsonObject data, final IxMod in) {
        final UxJooq jooq = IxPin.jooq(in);
        return Pre.qPk().inJAsync(data, in).compose(condition -> {
            Ix.Log.filters(this.getClass(), "( Unique ) By Pk: identifier: {0}, condition: {1}",
                in.module().getIdentifier(), condition);
            return jooq.fetchJOneAsync(condition);
        });
    }

    protected Future<JsonObject> fetchByUk(final JsonObject data, final IxMod in) {
        final UxJooq jooq = IxPin.jooq(in);
        return Pre.qUk().inJAsync(data, in).compose(condition -> {
            Ix.Log.filters(this.getClass(), "( Unique ) By Uk: identifier: {0}, condition: {1}",
                in.module().getIdentifier(), condition);
            return jooq.fetchJOneAsync(condition);
        });
    }

    @SafeVarargs
    protected final Future<JsonObject> runUnique(
        final JsonObject data,
        final IxMod in,
        final BiFunction<JsonObject, IxMod, Future<JsonObject>>... executors) {
        if (0 == executors.length) {
            return Ux.future(new JsonObject());
        }
        Future<JsonObject> first = executors[Values.IDX].apply(data, in);
        for (int start = 1; start < executors.length; start++) {
            final int current = start;
            first = first.compose(queried -> {
                if (Ut.isNil(queried)) {
                    return executors[current].apply(data, in);
                } else {
                    return Ux.future(queried);
                }
            });
        }
        return first;
    }
}
