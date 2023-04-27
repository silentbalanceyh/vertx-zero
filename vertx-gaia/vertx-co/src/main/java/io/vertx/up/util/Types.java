package io.vertx.up.util;

import io.horizon.util.HaS;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("all")
final class Types {
    private static final Annal LOGGER = Annal.get(Types.class);

    private Types() {
    }

    static <T> boolean isEqual(final JsonObject record, final String field, final T expected) {
        if (HaS.isNil(record)) {
            /*
             * If record is null or empty, return `false`
             */
            return false;
        } else {
            /*
             * Object reference
             */
            final Object value = record.getValue(field);
            return HaS.isSame(value, expected);
        }
    }

    @Deprecated
    static boolean isJArray(final String literal) {
        return Fn.orNull(Boolean.FALSE, () -> {
            try {
                new JsonArray(literal);
                return true;
            } catch (final DecodeException ex) {
                return false;
            }
        }, literal);
    }

    @Deprecated
    static boolean isJArray(final Object value) {
        return Fn.orSemi(null == value, LOGGER,
            () -> false,
            () -> isJArray(value.getClass()));
    }

    @Deprecated
    static boolean isJArray(final Class<?> clazz) {
        return JsonArray.class == clazz;
    }

    static boolean isArrayString(final JsonArray array) {
        if (Objects.isNull(array)) {
            return false;
        } else {
            return array.stream().allMatch(item -> item instanceof String);
        }
    }

    static boolean isArrayJson(final JsonArray array) {
        if (Objects.isNull(array)) {
            return false;
        } else {
            return array.stream().allMatch(item -> item instanceof JsonObject);
        }
    }

    static boolean isClass(final Object value) {
        return Fn.orSemi(null == value, LOGGER,
            () -> false,
            () -> null != Instance.clazz(value.toString()));
    }

    @Deprecated
    static boolean isJObject(final String literal) {
        return Fn.orNull(Boolean.FALSE, () -> {
            try {
                new JsonObject(literal);
                return true;
            } catch (final DecodeException ex) {
                return false;
            }
        }, literal);
    }

    @Deprecated
    static boolean isJObject(final Object value) {
        return Fn.orSemi(null == value, LOGGER,
            () -> false,
            () -> isJObject(value.getClass()));
    }

    @Deprecated
    static boolean isJObject(final Class<?> clazz) {
        return JsonObject.class == clazz || LinkedHashMap.class == clazz;
    }

    static boolean isIn(final JsonObject input, final String... fields) {
        if (Ut.isNil(input)) {
            return false;
        } else {
            final Set<String> fieldSet = Arrays.stream(fields).collect(Collectors.toSet());
            final long counter = input.fieldNames().stream()
                .filter(Objects::nonNull)
                .filter(fieldSet::contains)
                .map(input::getValue)
                .filter(item -> item instanceof String)
                .map(item -> (String) item)
                .filter(Ut::isNotNil)
                .count();
            return counter == fields.length;
        }
    }

    static boolean isArray(final Object value) {
        if (null == value) {
            return false;
        }
        return (value instanceof Collection ||
            value.getClass().isArray());
    }
}
