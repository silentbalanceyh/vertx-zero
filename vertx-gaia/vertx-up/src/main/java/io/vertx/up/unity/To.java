package io.vertx.up.unity;

import io.reactivex.Observable;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.pojo.Mirror;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.Record;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("all")
class To {

    static <T> Future<T> future(final T entity) {
        return Fn.getNull(Future.succeededFuture(),
                () -> Fn.getSemi(entity instanceof Throwable, null,
                        () -> Future.failedFuture((Throwable) entity),
                        () -> Future.succeededFuture(entity)),
                entity);
    }

    static <T> JsonObject toJson(
            final T entity,
            final String pojo) {
        return Fn.getNull(new JsonObject(),
                () -> Fn.getSemi(Ut.isNil(pojo), null,
                        () -> Ut.serializeJson(entity),
                        () -> Mirror.create(To.class)
                                .mount(pojo)
                                .connect(Ut.serializeJson(entity))
                                .to().result()),
                entity);
    }

    static <T> JsonObject toJson(
            final T entity,
            final Function<JsonObject, JsonObject> convert
    ) {
        return Fn.getSemi(null == convert, null,
                () -> toJson(entity, ""),
                () -> convert.apply(toJson(entity, "")));
    }

    static <T> JsonArray toJArray(
            final List<T> list,
            final Function<JsonObject, JsonObject> convert
    ) {
        final JsonArray array = new JsonArray();
        Observable.fromIterable(list)
                .filter(Objects::nonNull)
                .map(item -> toJson(item, convert))
                .subscribe(array::add);
        return array;
    }

    static JsonArray toJArray(final Record[] records) {
        final JsonArray result = new JsonArray();
        if (Objects.nonNull(records)) {
            Arrays.stream(records).map(Record::toJson)
                    .forEach(result::add);
        }
        return result;
    }

    static <T> JsonArray toJArray(
            final List<T> list,
            final String pojo
    ) {
        final JsonArray array = new JsonArray();
        Observable.fromIterable(list)
                .filter(Objects::nonNull)
                .map(item -> toJson(item, pojo))
                .subscribe(array::add);
        return array;
    }

    static <T> List<JsonObject> toJList(
            final List<T> list,
            final String pojo
    ) {
        final List<JsonObject> jlist = new ArrayList<>();
        Ut.itJArray(toJArray(list, pojo)).forEach(jlist::add);
        return jlist;
    }

    @SuppressWarnings("all")
    static <T> Envelop toEnvelop(
            final T entity
    ) {
        return Fn.getNull(Envelop.ok(),
                () -> Fn.getSemi(entity instanceof WebException, null,
                        () -> Envelop.failure((WebException) entity),
                        () -> {
                            if (Envelop.class == entity.getClass()) {
                                return (Envelop) entity;
                            } else {
                                return Envelop.success(entity);
                            }
                        }),
                entity);
    }

    static <T> Function<T, List<Future<T>>> toFutureList(final Function<T, Future<T>>... functions) {
        final List<Future<T>> futures = new ArrayList<>();
        return (entity) -> {
            Observable.fromArray(functions)
                    .map(function -> function.apply(entity))
                    .subscribe(futures::add).dispose();
            return futures;
        };
    }

    static <T> Envelop toEnvelop(
            final T entity,
            final WebException error
    ) {
        return Fn.getNull(Envelop.failure(error),
                () -> Envelop.success(entity), entity);
    }

    static WebException toError(
            final Class<? extends WebException> clazz,
            final Object... args
    ) {
        if (null == clazz || null == args) {
            // Fix Cast WebException error.
            return new _500InternalServerException(To.class, "clazz arg is null");
        } else {
            return Ut.instance(clazz, args);
        }
    }

    @SuppressWarnings("all")
    static WebException toError(
            final Class<?> clazz,
            final Throwable error
    ) {
        return Fn.getSemi(error instanceof WebException, null,
                () -> (WebException) error,
                () -> new _500InternalServerException(clazz, error.getMessage()));
    }

    static Envelop toEnvelop(
            final Class<? extends WebException> clazz,
            final Object... args
    ) {
        return Envelop.failure(toError(clazz, args));
    }

    static JsonObject toUnique(
            final JsonArray array,
            final String pojo
    ) {
        return Fn.getSemi(null == array || array.isEmpty(), null,
                () -> toJson(null, pojo),
                () -> toJson(array.getValue(0), pojo));
    }

    static JsonObject toToggle(final Object... args) {
        final JsonObject params = new JsonObject();
        for (int idx = 0; idx < args.length; idx++) {
            final String idxStr = String.valueOf(idx);
            params.put(idxStr, args[idx]);
        }
        return params;
    }
}
