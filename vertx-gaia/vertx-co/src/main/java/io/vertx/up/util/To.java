package io.vertx.up.util;

import io.vertx.core.MultiMap;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

final class To {

    private To() {
    }

    static <T extends Enum<T>> T toEnum(final Class<T> clazz, final String input) {
        return Fn.getJvm(null, () -> Enum.valueOf(clazz, input), clazz, input);
    }

    static <T extends Enum<T>> T toEnum(final Supplier<String> supplier, final Class<T> type, final T defaultEnum) {
        final String method = supplier.get();
        T result = Ut.toEnum(type, method);
        if (null == result) {
            result = defaultEnum;
        }
        return result;
    }

    static Integer toInteger(final Object value) {
        return Fn.getNull(null, () -> Integer.parseInt(value.toString()), value);
    }

    static Set<String> toSet(final JsonArray keys) {
        final JsonArray keysData = Define.sureJArray(keys);
        final Set<String> keySet = new HashSet<>();
        It.itJString(keysData).forEach(keySet::add);
        return keySet;
    }

    static String toString(final Object reference) {
        return Fn.getNull("null", () -> {
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

    static <T> JsonArray toJArray(final T entity, final int repeat) {
        final JsonArray array = new JsonArray();
        for (int idx = Values.IDX; idx < repeat; idx++) {
            array.add(entity);
        }
        return array;
    }

    static Collection<?> toCollection(final Object value) {
        return Fn.getNull(() -> {
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
}
