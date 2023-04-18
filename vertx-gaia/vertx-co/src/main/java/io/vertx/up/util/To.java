package io.vertx.up.util;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Record;
import io.vertx.up.eon.Values;
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

    static <T extends Enum<T>> T toEnum(final Class<T> clazz, final String input) {
        return Fn.orJvm(null, () -> Enum.valueOf(clazz, input), clazz, input);
    }

    static <T extends Enum<T>> T toEnum(final Supplier<String> supplier, final Class<T> type, final T defaultEnum) {
        final String method = supplier.get();
        T result = Ut.toEnum(type, method);
        if (null == result) {
            result = defaultEnum;
        }
        return result;
    }

    static HttpMethod toMethod(final Supplier<String> supplier, final HttpMethod defaultValue) {
        final String method = supplier.get();
        if (StringUtil.isNil(method)) {
            return defaultValue;
        } else {
            return HttpMethod.valueOf(method);
        }
    }

    static Integer toInteger(final Object value) {
        return Fn.orNull(null, () -> Integer.parseInt(value.toString()), value);
    }

    static List<Class<?>> toClass(final JsonArray names) {
        final JsonArray keysData = Jackson.sureJArray(names);
        final List<Class<?>> classList = new ArrayList<>();
        It.itJString(keysData)
            .map(name -> Instance.clazz(name, null))
            .filter(Objects::nonNull)
            .forEach(classList::add);
        return classList;
    }

    static List<String> toList(final JsonArray keys) {
        final JsonArray keysData = Jackson.sureJArray(keys);
        final List<String> keyList = new ArrayList<>();
        It.itJString(keysData).forEach(keyList::add);
        return keyList;
    }

    static String toString(final Object reference) {
        return Fn.orNull("null", () -> {
            final String literal;
            if (Types.isJObject(reference)) {
                // Fix issue for serialization
                literal = ((JsonObject) reference).encode();
            } else if (Types.isJArray(reference)) {
                // Fix issue for serialization
                literal = ((JsonArray) reference).encode();
            } else {
                literal = reference.toString();
            }
            return literal;
        }, reference);
    }

    static JsonArray toJArray(final JsonArray array, final Function<JsonObject, JsonObject> executor) {
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(array).map(executor).forEach(normalized::add);
        return normalized;
    }

    static <T> JsonArray toJArray(final Set<T> set) {
        final JsonArray array = new JsonArray();
        if (Objects.nonNull(set)) {
            set.stream().filter(Objects::nonNull).forEach(array::add);
        }
        return array;
    }

    static <T> JsonArray toJArray(final List<T> list) {
        final JsonArray array = new JsonArray();
        if (Objects.nonNull(list)) {
            list.stream().filter(Objects::nonNull).forEach(array::add);
        }
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

    static JsonObject toJObject(final String literal) {
        if (Ut.isNil(literal)) {
            return new JsonObject();
        } else {
            try {
                return new JsonObject(literal);
            } catch (final DecodeException ex) {
                return new JsonObject();
            }
        }
    }

    static JsonObject toJObject(final String literal, final Function<JsonObject, JsonObject> itemFn) {
        final JsonObject parsed = toJObject(literal);
        return Objects.isNull(itemFn) ? parsed : itemFn.apply(parsed);
    }

    static JsonArray toJArray(final String literal) {
        if (Ut.isNil(literal)) {
            return new JsonArray();
        } else {
            try {
                return new JsonArray(literal);
            } catch (final DecodeException ex) {
                return new JsonArray();
            }
        }
    }

    static JsonArray toJArray(final String literal, final Function<JsonObject, JsonObject> itemFn) {
        final JsonArray parsed = toJArray(literal);
        if (Objects.isNull(itemFn)) {
            return parsed;
        } else {
            final JsonArray replaced = new JsonArray();
            parsed.forEach(item -> {
                if (item instanceof JsonObject) {
                    replaced.add(itemFn.apply((JsonObject) item));
                } else {
                    // Fix String Literal Serialization
                    replaced.add(item);
                }
            });
            // Ut.itJArray(parsed).map(itemFn).forEach(replaced::add);
            return replaced;
        }
    }

    static <T> JsonArray toJArray(final T entity, final int repeat) {
        final JsonArray array = new JsonArray();
        for (int idx = Values.IDX; idx < repeat; idx++) {
            array.add(entity);
        }
        return array;
    }

    static Collection<?> toCollection(final Object value) {
        return Fn.orNull(() -> {
            // Collection
            if (value instanceof Collection) {
                return ((Collection<?>) value);
            }
            // JsonArray
            if (Types.isJArray(value)) {
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

    static JsonObject toJObject(final Map<String, Object> map) {
        final JsonObject params = new JsonObject();
        Fn.safeNull(() -> map.forEach(params::put), map);
        return params;
    }

    static <T> JsonObject toJObject(final ConcurrentMap<String, T> map) {
        final JsonObject params = new JsonObject();
        Fn.safeNull(() -> map.forEach(params::put), map);
        return params;
    }

    static JsonObject toJObject(final MultiMap multiMap) {
        final JsonObject params = new JsonObject();
        Fn.safeNull(() -> multiMap.forEach(
            item -> params.put(item.getKey(), item.getValue())
        ), multiMap);
        return params;
    }

    static Class<?> toPrimary(final Class<?> source) {
        return Types.UNBOXES.getOrDefault(source, source);
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
        return Fn.orSemi(error instanceof WebException, null,
            () -> (WebException) error,
            () -> new _500InternalServerException(clazz, error.getMessage()));
    }
}
