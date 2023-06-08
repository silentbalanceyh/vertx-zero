package io.vertx.mod.crud.refine;

import io.aeon.experiment.specification.KModule;
import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.uca.aop.Aspect;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.init.IxPin;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.atom.shape.KField;
import io.vertx.up.atom.shape.KJoin;
import io.vertx.up.atom.shape.KPoint;
import io.vertx.up.uca.jooq.UxJoin;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IxFn {

    static Function<JsonObject, Future<JsonObject>> fileFn(final IxMod in,
                                                           final BiFunction<JsonObject, JsonArray, Future<JsonArray>> fileFn) {
        return data -> {
            final KModule module = in.module();
            final KField field = module.getField();
            if (Objects.isNull(field)) {
                /*
                 * KField of new attachment:
                 * {
                 *      "attachment": [
                 *          {
                 *              "field": "model field name",
                 *              "condition": {
                 *                  "field1": "value1",
                 *                  "field2": "value2"
                 *              }
                 *          }
                 *      ]
                 * }
                 */
                return Ux.future(data);
            }
            final ConcurrentMap<String, JsonObject> attachmentMap = field.fieldFile();
            return Ke.mapFn(attachmentMap, fileFn).apply(data);
        };
    }

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

    // JqFn
    @SafeVarargs
    static <T> Future<T> passion(final T input, final IxMod in, final BiFunction<T, IxMod, Future<T>>... executors) {
        // Sequence for future management
        Future<T> future = Future.succeededFuture(input);
        for (final BiFunction<T, IxMod, Future<T>> executor : executors) {
            if (Objects.nonNull(executor)) {
                future = future.compose(data -> executor.apply(data, in));
            }
        }
        return future;
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
                if (object instanceof final JsonObject json) {
                    /*
                     * Json Object Processing
                     */
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
                    final JsonObject json = records.getJsonObject(VValue.IDX);
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
                    filters.put(VString.EMPTY, Boolean.FALSE);
                }
                return executor.apply(switchedJq, filters);
            }
        };
    }

    static <T> Function<T, Future<T>> wrap(
        final KModule module, final BiFunction<Aspect, Function<T, Future<T>>, Function<T, Future<T>>> aopFn,
        final Function<T, Future<T>> executor) {
        return input -> {
            final JsonObject aop = module.getAop();
            if (Ut.isNil(aop)) {
                return executor.apply(input);
            } else {
                final Aspect aspect = Aspect.create(aop);
                return aopFn.apply(aspect, executor).apply(input);
            }
        };
    }
}
