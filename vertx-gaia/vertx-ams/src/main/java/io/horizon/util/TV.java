package io.horizon.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lang : 2023/4/28
 */
class TV {


    static <T> Set<String> vStringSet(final Collection<T> list, final Function<T, String> supplier) {
        return list.stream()
            .filter(Objects::nonNull)
            .map(supplier)
            .filter(HaS::isNotNil)
            .collect(Collectors.toSet());
    }

    static Set<String> vStringSet(final JsonArray array, final String field) {
        Set<String> set = new HashSet<>();
        if (Objects.nonNull(array)) {
            set = array.stream()
                .filter(item -> item instanceof JsonObject)
                .map(item -> (JsonObject) item)
                .filter(item -> item.getValue(field) instanceof String)
                .map(item -> item.getString(field))
                .filter(HaS::isNotNil)
                .collect(Collectors.toSet());
        }
        return set;
    }

    static String vString(final JsonArray array, final String field) {
        final Set<String> set = new HashSet<>();
        HaS.itJArray(array).map(json -> json.getString(field))
            .filter(Objects::nonNull)
            .forEach(set::add);
        if (1 == set.size()) {
            return set.iterator().next();
        } else {
            return null;
        }
    }

    static Set<JsonArray> vArraySet(final JsonArray array, final String field) {
        Set<JsonArray> set = new HashSet<>();
        if (Objects.nonNull(array)) {
            set = array.stream()
                .filter(item -> item instanceof JsonObject)
                .map(item -> (JsonObject) item)
                .filter(item -> item.getValue(field) instanceof JsonArray)
                .map(item -> item.getJsonArray(field))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        }
        return set;
    }

    static String vString(final JsonObject json, final String field, final String defaultValue) {
        final JsonObject inputJ = HJson.valueJObject(json, false);
        final Object valueS = inputJ.getValue(field, defaultValue);
        return Objects.isNull(valueS) ? null : valueS.toString();
    }

    static Integer vInt(final JsonObject json, final String field, final Integer defaultInt) {
        final String literal = vString(json, field, String.valueOf(defaultInt));
        if (Objects.isNull(literal) || !TNumeric.isInteger(literal)) {
            return defaultInt;
        }
        return Integer.valueOf(literal);
    }

    static Class<?> vClass(final JsonObject json, final String field, final Class<?> defaultValue) {
        final String clsStr = vString(json, field, null);
        if (TIs.isNil(clsStr)) {
            return defaultValue;
        }
        return HaS.clazz(clsStr, defaultValue);
    }

    static Class<?> vClass(final JsonObject json, final String field,
                           final Class<?> interfaceCls,
                           final Class<?> defaultValue) {
        final String clsStr = vString(json, field, null);
        final Class<?> implCls;
        if (TIs.isNil(clsStr)) {
            implCls = defaultValue;
        } else {
            implCls = HaS.clazz(clsStr, defaultValue);
        }
        if (Objects.isNull(implCls)) {
            return null;
        }
        if (HaS.isImplement(implCls, interfaceCls)) {
            return implCls;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    static <T> T vT(final JsonObject item, final String field, final Class<T> clazz) {
        if (TIs.isNil(item)) {
            return null;
        } else {
            final Object value = item.getValue(field);
            if (Objects.isNull(value)) {
                return null;
            } else {
                if (clazz == value.getClass()) {
                    return (T) value;
                } else {
                    return null;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    static <T> T vT(final String literal, final Class<?> type) {
        if (TIs.isNil(literal)) {
            return null;
        }
        if (type == Integer.class || type == int.class) {
            return (T) Integer.valueOf(literal);
        } else if (type == Long.class || type == long.class) {
            return (T) Long.valueOf(literal);
        } else if (type == Float.class || type == float.class) {
            return (T) Float.valueOf(literal);
        } else if (type == Double.class || type == double.class) {
            return (T) Double.valueOf(literal);
        } else if (type == Boolean.class || type == boolean.class) {
            return (T) Boolean.valueOf(literal);
        } else if (type == String.class) {
            return (T) literal;
        } else if (type == BigDecimal.class) {
            return (T) new BigDecimal(literal);
        } else if (type == BigInteger.class) {
            return (T) new BigInteger(literal);
        } else if (type == LocalDate.class) {
            return (T) LocalDate.parse(literal);
        } else if (type == LocalDateTime.class) {
            return (T) LocalDateTime.parse(literal);
        } else if (type == LocalTime.class) {
            return (T) LocalTime.parse(literal);
        } else if (type == JsonArray.class) {
            return (T) HJson.toJArray(literal);
        } else if (type == JsonObject.class) {
            return (T) HJson.toJObject(literal);
        } else if (type == List.class || type == ArrayList.class) {
            return (T) Arrays.asList(literal.split("\\s*,\\s*"));
        } else if (type == Set.class || type == HashSet.class) {
            return (T) new HashSet<>(Arrays.asList(literal.split("\\s*,\\s*")));
        } else if (type == String[].class) {
            return (T) Arrays.stream(literal.split("\\s*,\\s*")).toArray(String[]::new);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

}
