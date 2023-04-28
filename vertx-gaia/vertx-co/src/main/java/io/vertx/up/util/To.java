package io.vertx.up.util;

import io.horizon.util.HaS;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.fn.Fn;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

final class To {

    private To() {
    }

    @SuppressWarnings("all")
    static <V> ConcurrentMap<String, V> toMap(final JsonObject data) {
        final ConcurrentMap<String, V> map = new ConcurrentHashMap<>();
        if (Objects.nonNull(data)) {
            data.fieldNames().forEach(field -> {
                final Object value = data.getValue(field);
                if (Objects.nonNull(value)) {
                    map.put(field, (V) value);
                }
            });
        }
        return map;
    }

    static <T> Map<String, Object> toMapExpr(final JsonObject data) {
        // Serialized
        final Map<String, Object> map = new HashMap<>();
        data.getMap().forEach((key, value) -> {
            if (value instanceof JsonObject) {
                map.put(key, toMapExpr((JsonObject) value));
            } else if (value instanceof JsonArray) {
                map.put(key, toMapExpr((JsonArray) value));
            } else {
                map.put(key, value);
            }
        });
        return map;
    }

    @SuppressWarnings("unchecked")
    private static List<Object> toMapExpr(final JsonArray data) {
        final List<Object> nested = new ArrayList<>();
        data.getList().forEach(item -> {
            if (item instanceof JsonObject) {
                nested.add(toMapExpr((JsonObject) item));
            } else if (item instanceof JsonArray) {
                nested.add(toMapExpr((JsonArray) item));
            } else {
                nested.add(item);
            }
        });
        return nested;
    }

    static HttpMethod toMethod(final Supplier<String> supplier, final HttpMethod defaultValue) {
        final String method = supplier.get();
        if (HaS.isNil(method)) {
            return defaultValue;
        } else {
            return HttpMethod.valueOf(method);
        }
    }

    static String toString(final Object reference) {
        return Fn.runOr("null", () -> {
            final String literal;
            if (HaS.isJObject(reference)) {
                // Fix issue for serialization
                literal = ((JsonObject) reference).encode();
            } else if (HaS.isJArray(reference)) {
                // Fix issue for serialization
                literal = ((JsonArray) reference).encode();
            } else {
                literal = reference.toString();
            }
            return literal;
        }, reference);
    }

    static JsonObject toJObject(final String literal, final Function<JsonObject, JsonObject> itemFn) {
        final JsonObject parsed = HaS.toJObject(literal);
        return Objects.isNull(itemFn) ? parsed : itemFn.apply(parsed);
    }

    static Collection<?> toCollection(final Object value) {
        return Fn.runOr(() -> {
            // Collection
            if (value instanceof Collection) {
                return ((Collection<?>) value);
            }
            // JsonArray
            if (HaS.isJArray(value)) {
                return ((JsonArray) value).getList();
            }
            // Object[]
            if (Types.isArray(value)) {
                // Array
                final Object[] values = (Object[]) value;
                return Arrays.asList(values);
            }
            return null;
        }, value);
    }

    static JsonObject toJObject(final MultiMap multiMap) {
        final JsonObject params = new JsonObject();
        Fn.runAt(() -> multiMap.forEach(
            item -> params.put(item.getKey(), item.getValue())
        ), multiMap);
        return params;
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
        return Fn.runOr(error instanceof WebException, null,
            () -> (WebException) error,
            () -> new _500InternalServerException(clazz, error.getMessage()));
    }
}
