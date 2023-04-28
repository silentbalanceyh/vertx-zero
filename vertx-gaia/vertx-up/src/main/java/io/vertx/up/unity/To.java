package io.vertx.up.unity;

import io.reactivex.Observable;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.pojo.Mirror;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.WebException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings("all")
class To {

    static JsonObject subset(final JsonObject input, final Set<String> removed) {
        removed.forEach(input::remove);
        return input;
    }

    static JsonArray subset(final JsonArray input, final Set<String> removed) {
        Ut.itJArray(input).forEach(json -> subset(json, removed));
        return input;
    }

    static <T> Future<T> future(final T entity) {
        return Fn.runOr(Future.succeededFuture(),
            () -> Fn.orSemi(entity instanceof Throwable, null,
                () -> Future.failedFuture((Throwable) entity),
                () -> Future.succeededFuture(entity)),
            entity);
    }

    static <T> JsonObject toJObject(
        final T entity,
        final String pojo) {
        return Fn.runOr(new JsonObject(),
            () -> Fn.orSemi(Ut.isNil(pojo), null,
                // Turn On Smart
                () -> Ut.serializeJson(entity, true),
                () -> Mirror.create(To.class).mount(pojo).connect(Ut.serializeJson(entity, true)).to().result()),
            entity);
    }

    static <T> JsonObject toJObject(
        final T entity,
        final Function<JsonObject, JsonObject> convert
    ) {
        return Fn.orSemi(null == convert, null,
            () -> toJObject(entity, ""),
            () -> convert.apply(toJObject(entity, "")));
    }

    static <T> JsonArray toJArray(
        final List<T> list,
        final Function<JsonObject, JsonObject> convert
    ) {
        return Fn.runOr(new JsonArray(), () -> {
            final JsonArray array = new JsonArray();
            Observable.fromIterable(list)
                .filter(Objects::nonNull)
                .map(item -> toJObject(item, convert))
                .subscribe(array::add);
            return array;
        }, list);
    }

    static <T> JsonArray toJArray(
        final List<T> list,
        final String pojo
    ) {
        return Fn.runOr(new JsonArray(), () -> {
            final JsonArray array = new JsonArray();
            Observable.fromIterable(list)
                .filter(Objects::nonNull)
                .map(item -> toJObject(item, pojo))
                .subscribe(array::add);
            return array;
        }, list);
    }

    static <T> List<JsonObject> toJList(
        final List<T> list,
        final String pojo
    ) {
        return Fn.runOr(new ArrayList<>(), () -> {
            final List<JsonObject> jlist = new ArrayList<>();
            Ut.itJArray(toJArray(list, pojo)).forEach(jlist::add);
            return jlist;
        }, list);
    }

    @SuppressWarnings("all")
    static <T> Envelop toEnvelop(
        final T entity
    ) {
        return Fn.runOr(Envelop.ok(), () -> Fn.orSemi(entity instanceof WebException, null,
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

    static <T> Envelop toEnvelop(
        final T entity,
        final WebException error
    ) {
        return Fn.runOr(Envelop.failure(error),
            () -> Envelop.success(entity), entity);
    }

    static Envelop toEnvelop(
        final Class<? extends WebException> clazz,
        final Object... args
    ) {
        return Envelop.failure(Ut.toError(clazz, args));
    }

    static JsonObject toUnique(
        final JsonArray array,
        final String pojo
    ) {
        return Fn.orSemi(null == array || array.isEmpty(), null,
            () -> toJObject(null, pojo),
            () -> toJObject(array.getValue(0), pojo));
    }

    static JsonObject toToggle(final Object... args) {
        final JsonObject params = new JsonObject();
        for (int idx = 0; idx < args.length; idx++) {
            final String idxStr = String.valueOf(idx);
            params.put(idxStr, args[idx]);
        }
        return params;
    }

    static <T, R> JsonObject toMerge(final T input, final String field, final List<R> list) {
        if (Objects.isNull(input)) {
            return new JsonObject();
        } else {
            final JsonObject serialized = Ut.serializeJson(input);
            if (Objects.nonNull(list)) {
                final JsonArray listData = Ut.serializeJson(list);
                serialized.put(field, listData);
            }
            return serialized;
        }
    }
}
