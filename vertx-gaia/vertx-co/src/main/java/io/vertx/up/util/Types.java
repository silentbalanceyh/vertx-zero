package io.vertx.up.util;

import io.horizon.util.HH;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@SuppressWarnings("all")
final class Types {
    static final ConcurrentMap<Class<?>, Class<?>> UNBOXES =
        new ConcurrentHashMap<Class<?>, Class<?>>() {
            {
                this.put(Integer.class, int.class);
                this.put(Long.class, long.class);
                this.put(Short.class, short.class);
                this.put(Boolean.class, boolean.class);
                this.put(Character.class, char.class);
                this.put(Double.class, double.class);
                this.put(Float.class, float.class);
                this.put(Byte.class, byte.class);
            }
        };
    private static final Annal LOGGER = Annal.get(Types.class);

    private Types() {
    }

    /*
     * Whether record contains all the data in cond.
     * JsonObject subset here for checking
     */
    static boolean isSubset(final JsonObject cond, final JsonObject record) {
        final Set<String> fields = cond.fieldNames();
        final long counter = fields.stream()
            /* record contains all cond */
            .filter(record::containsKey)
            .filter(field -> HH.isEqual(record.getValue(field), cond.getValue(field)))
            .count();
        return fields.size() == counter;
    }

    static <T> boolean isEqual(final JsonObject record, final String field, final T expected) {
        if (HH.isNil(record)) {
            /*
             * If record is null or empty, return `false`
             */
            return false;
        } else {
            /*
             * Object reference
             */
            final Object value = record.getValue(field);
            return HH.isEqual(value, expected);
        }
    }

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

    static boolean isJArray(final Object value) {
        return Fn.orSemi(null == value, LOGGER,
            () -> false,
            () -> isJArray(value.getClass()));
    }

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

    static boolean isJObject(final Object value) {
        return Fn.orSemi(null == value, LOGGER,
            () -> false,
            () -> isJObject(value.getClass()));
    }

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

    static boolean isInteger(final Object value) {
        return Fn.orSemi(null == value, LOGGER,
            () -> false,
            () -> Numeric.isInteger(value.toString()));
    }

    static boolean isDecimal(final Object value) {
        return Fn.orSemi(null == value, LOGGER,
            () -> false,
            () -> Numeric.isDecimal(value.toString()));
    }

    static boolean isDate(final Object value) {
        if (Objects.isNull(value)) {
            return false;
        } else {
            if (value instanceof Class) {
                final Class<?> type = (Class<?>) value;
                return HH.isDate(type);
            } else {
                return Period.isValid(value.toString());
            }
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
