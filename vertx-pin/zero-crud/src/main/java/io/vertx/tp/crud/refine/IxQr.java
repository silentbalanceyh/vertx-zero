package io.vertx.tp.crud.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.tp.ke.atom.connect.KJoin;
import io.vertx.tp.ke.atom.connect.KPoint;
import io.vertx.up.uca.jooq.UxJoin;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IxQr {
    static Function<JsonObject, Future<JsonArray>> fetchFn(final IxIn in) {
        return condition -> {
            // KModule
            final KModule connect = in.connect();
            if (Objects.isNull(connect)) {
                // Direct
                final UxJooq jooq = IxPin.jooq(in);
                return jooq.fetchJAsync(condition);
            } else {
                // Join
                final UxJoin join = IxPin.join(in, connect);
                return join.fetchAsync(condition);
            }
        };
    }

    static Function<JsonObject, Future<JsonObject>> searchFn(final IxIn in) {
        return condition -> {
            // KModule
            final KModule connect = in.connect();
            if (Objects.isNull(connect)) {
                // Direct
                final UxJooq jooq = IxPin.jooq(in);
                return jooq.searchAsync(condition);
            } else {
                // Join
                final UxJoin join = IxPin.join(in, connect);
                return join.searchAsync(condition);
            }
        };
    }

    static Function<JsonObject, Future<Long>> countFn(final IxIn in) {
        return condition -> {
            // KModule
            final KModule connect = in.connect();
            if (Objects.isNull(connect)) {
                // Direct
                final UxJooq jooq = IxPin.jooq(in);
                return jooq.countAsync(condition);
            } else {
                // Join
                final UxJoin join = IxPin.join(in, connect);
                return join.countAsync(condition);
            }
        };
    }

    static Function<JsonObject, Future<JsonObject>> seekFn(final IxIn in) {
        return entityJson -> {
            final KModule module = in.module();
            final KJoin join = module.getConnect();
            if (Objects.isNull(join)) {
                return Ux.future(new JsonObject());
            } else {
                final KPoint point = join.procTarget(entityJson);
                /*
                 * IxDao for Jooq
                 * */
                final KModule switched = IxPin.getActor(point.getCrud());
                final UxJooq switchedJq = IxPin.jooq(switched, in.envelop());
                /*
                 * Filters
                 */
                final JsonObject filters = new JsonObject();
                join.procFilters(entityJson, point, filters);
                if (Ut.isNil(switched.getPojo())) {
                    switchedJq.on(switched.getPojo());
                }
                return switchedJq.fetchJOneAsync(filters);
            }
        };
    }
}
