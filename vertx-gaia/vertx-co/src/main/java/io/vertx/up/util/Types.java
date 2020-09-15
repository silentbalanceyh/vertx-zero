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
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

    static boolean isJArray(final String literal) {
        return Fn.getNull(Boolean.FALSE, () -> {
            try {
                new JsonArray(literal);
                return true;
            } catch (final DecodeException ex) {
                return false;
            }
        }, literal);
    }

    static boolean isJArray(final Object value) {
        return Fn.getSemi(null == value, LOGGER,
                () -> false,
                () -> isJArray(value.getClass()));
    }

    static boolean isJArray(final Class<?> clazz) {
        return JsonArray.class == clazz;
    }

    static boolean isVoid(final Class<?> clazz) {
        return null == clazz || Void.class == clazz || void.class == clazz;
    }

    static boolean isClass(final Object value) {
        return Fn.getSemi(null == value, LOGGER,
                () -> false,
                () -> null != Instance.clazz(value.toString()));
    }

    static boolean isJObject(final String literal) {
        return Fn.getNull(Boolean.FALSE, () -> {
            try {
                new JsonObject(literal);
                return true;
            } catch (final DecodeException ex) {
                return false;
            }
        }, literal);
    }

    static boolean isEmpty(final JsonObject json) {
        return Fn.getNull(Boolean.TRUE, () -> 0 == json.fieldNames().size(), json);
    }

    static boolean isEmpty(final JsonArray jsonArray) {
        if (Objects.isNull(jsonArray) || jsonArray.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    static boolean isJObject(final Object value) {
        return Fn.getSemi(null == value, LOGGER,
                () -> false,
                () -> isJObject(value.getClass()));
    }

    static boolean isJObject(final Class<?> clazz) {
        return JsonObject.class == clazz || LinkedHashMap.class == clazz;
    }

    static boolean isInteger(final Object value) {
        return Fn.getSemi(null == value, LOGGER,
                () -> false,
                () -> Numeric.isInteger(value.toString()));
    }

    static boolean isInteger(final Class<?> clazz) {
        return int.class == clazz || Integer.class == clazz
                || long.class == clazz || Long.class == clazz
                || short.class == clazz || Short.class == clazz;
    }

    static boolean isDecimal(final Object value) {
        return Fn.getSemi(null == value, LOGGER,
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
        return Fn.getSemi(null == value, LOGGER,
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
