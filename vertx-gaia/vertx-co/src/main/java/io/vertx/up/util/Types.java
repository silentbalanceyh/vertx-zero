package io.vertx.up.util;

import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    private static boolean equal(final Object left, final Object right) {
        if (null == left && null == right) {
            return true;
        } else if (null == left && null != right) {
            return false;
        } else if (null != left && null == right) {
            return false;
        } else {
            return left.equals(right);
        }
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
            .filter(field -> equal(record.getValue(field), cond.getValue(field)))
            .count();
        return fields.size() == counter;
    }

    static boolean isEqual(final String left, final String right) {
        return equal(left, right);
    }

    static <T> boolean isEqual(final JsonObject record, final String field, final T expected) {
        if (isEmpty(record)) {
            /*
             * If record is null or empty, return `false`
             */
            return false;
        } else {
            /*
             * Object reference
             */
            final Object value = record.getValue(field);
            return equal(value, expected);
        }
    }

    /*
     * Low performance processing, be careful to use
     */
    static boolean isUUID(final String literal) {
        UUID converted;
        try {
            converted = UUID.fromString(literal);
        } catch (final IllegalArgumentException ex) {
            converted = null;
        }
        return Objects.nonNull(converted);
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

    static boolean isVoid(final Class<?> clazz) {
        return null == clazz || Void.class == clazz || void.class == clazz;
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

    static boolean isEmpty(final JsonObject json) {
        return Fn.orNull(Boolean.TRUE, () -> 0 == json.fieldNames().size(), json);
    }

    static boolean isEmpty(final JsonArray jsonArray) {
        if (Objects.isNull(jsonArray) || jsonArray.isEmpty()) {
            return true;
        } else {
            return false;
        }
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
                .filter(Ut::notNil)
                .count();
            return counter == fields.length;
        }
    }

    static boolean isInteger(final Object value) {
        return Fn.orSemi(null == value, LOGGER,
            () -> false,
            () -> Numeric.isInteger(value.toString()));
    }

    static boolean isInteger(final Class<?> clazz) {
        return int.class == clazz || Integer.class == clazz
            || long.class == clazz || Long.class == clazz
            || short.class == clazz || Short.class == clazz;
    }

    static boolean isDecimal(final Object value) {
        return Fn.orSemi(null == value, LOGGER,
            () -> false,
            () -> Numeric.isDecimal(value.toString()));
    }

    static boolean isDecimal(final Class<?> clazz) {
        return double.class == clazz || Double.class == clazz
            || float.class == clazz || Float.class == clazz
            || BigDecimal.class == clazz;
    }

    static boolean isBoolean(final Class<?> clazz) {
        return boolean.class == clazz || Boolean.class == clazz;
    }

    static boolean isBoolean(final Object value) {
        return Fn.orSemi(null == value, LOGGER,
            () -> false,
            () -> {
                boolean logical = false;
                final String literal = value.toString();
                // Multi true literal such as "true", "TRUE" or 1
                if (Values.TRUE.equalsIgnoreCase(literal)
                    || Integer.valueOf(1).toString().equalsIgnoreCase(literal)
                    || Values.FALSE.equalsIgnoreCase(literal)
                    || Integer.valueOf(0).toString().equalsIgnoreCase(literal)) {
                    logical = true;
                }
                return logical;
            });
    }

    static boolean isDate(final Object value) {
        if (Objects.isNull(value)) {
            return false;
        } else {
            if (value instanceof Class) {
                final Class<?> type = (Class<?>) value;
                return isDate(type);
            } else {
                return Period.isValid(value.toString());
            }
        }
    }

    static boolean isDate(final Class<?> type) {
        return LocalDateTime.class == type || LocalDate.class == type ||
            LocalTime.class == type || Date.class == type ||
            Instant.class == type;
    }

    static boolean isArray(final Object value) {
        if (null == value) {
            return false;
        }
        return (value instanceof Collection ||
            value.getClass().isArray());
    }


    /**
     * Check Primary
     *
     * @param source
     *
     * @return
     */
    static boolean isPrimary(final Class<?> source) {
        return UNBOXES.values().contains(source);
    }
}
