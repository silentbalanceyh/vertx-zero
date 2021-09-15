package io.vertx.tp.crud.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.atom.specification.KJoin;
import io.vertx.tp.ke.atom.specification.KModule;
import io.vertx.tp.ke.atom.specification.KPoint;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.uca.jooq.UxJoin;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IxQr {
    static Function<JsonObject, Future<JsonArray>> fetchFn(final IxMod in) {
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

    static Function<JsonObject, Future<JsonObject>> searchFn(final IxMod in) {
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

    static Function<JsonObject, Future<Long>> countFn(final IxMod in) {
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

    static <T> BiFunction<Supplier<T>, BiFunction<UxJooq, JsonObject, Future<T>>, Future<T>> seekFn(final IxMod in, final Object object) {
        return (supplier, executor) -> {
            final KModule module = in.module();
            final KJoin join = module.getConnect();
            if (Objects.isNull(join)) {
                return Ux.future(supplier.get());
            } else {
                final UxJooq switchedJq;
                final JsonObject filters = new JsonObject();
                if (object instanceof JsonObject) {
                    /*
                     * Json Object Processing
                     */
                    final JsonObject json = (JsonObject) object;
                    final KPoint point = join.point(json);
                    final KModule switched = IxPin.getActor(point.getCrud());
                    switchedJq = IxPin.jooq(switched, in.envelop());

                    /* Filters For Record */
                    join.dataIn(json, point, filters);
                    if (Ut.isNil(switched.getPojo())) {
                        switchedJq.on(switched.getPojo());
                    }
                } else {
                    /*
                     * Json Array Processing
                     */
                    final JsonArray records = (JsonArray) object;
                    final KPoint point;
                    final JsonObject json = records.getJsonObject(Values.IDX);
                    if (Ut.isNil(json)) {
                        // Error to call this Api
                        switchedJq = null;
                        point = null;
                    } else {
                        point = join.point(json);
                        final KModule switched = IxPin.getActor(point.getCrud());
                        switchedJq = IxPin.jooq(switched, in.envelop());
                    }
                    if (Objects.nonNull(switchedJq)) {
                        /* Filters for Records */
                        Ut.itJArray(records).forEach(each -> {
                            final JsonObject single = new JsonObject();
                            join.dataIn(each, point, single);
                            final String key = "$" + single.hashCode();
                            filters.put(key, single);
                        });
                    }
                    /* Multi Condition for Processing */
                    filters.put(Strings.EMPTY, Boolean.FALSE);
                }
                return executor.apply(switchedJq, filters);
            }
        };
    }
}
