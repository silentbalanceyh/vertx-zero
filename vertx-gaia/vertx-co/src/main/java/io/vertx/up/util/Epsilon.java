package io.vertx.up.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.exchange.BMapping;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * Mapping data to List / JsonArray here
 * [],[],[],[]
 * Mapped by field here for different usage
 */
class Epsilon {

    static <T> Set<String> vStringSet(final Collection<T> list, final Function<T, String> supplier) {
        return list.stream()
            .filter(Objects::nonNull)
            .map(supplier)
            .filter(Ut::notNil)
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
                .filter(Ut::notNil)
                .collect(Collectors.toSet());
        }
        return set;
    }

    static String vString(final JsonArray array, final String field) {
        final Set<String> set = new HashSet<>();
        Ut.itJArray(array).map(json -> json.getString(field))
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
        final JsonObject inputJ = Jackson.sureJObject(json);
        final Object valueS = inputJ.getValue(field, defaultValue);
        return Objects.isNull(valueS) ? null : valueS.toString();
    }

    static Integer vInt(final JsonObject json, final String field, final Integer defaultInt) {
        final String literal = vString(json, field, String.valueOf(defaultInt));
        if (Objects.isNull(literal) || !Numeric.isInteger(literal)) {
            return defaultInt;
        }
        return Integer.valueOf(literal);
    }

    static Class<?> vClass(final JsonObject json, final String field, final Class<?> defaultValue) {
        final String clsStr = vString(json, field, null);
        if (Ut.isNil(clsStr)) {
            return defaultValue;
        }
        return Instance.clazz(clsStr, defaultValue);
    }

    static String vQrField(final String field, final String strOp) {
        return Ut.isNil(strOp) ? field : field + Strings.COMMA + strOp;
    }

    static Class<?> vClass(final JsonObject json, final String field,
                           final Class<?> interfaceCls,
                           final Class<?> defaultValue) {
        final String clsStr = vString(json, field, null);
        final Class<?> implCls;
        if (Ut.isNil(clsStr)) {
            implCls = defaultValue;
        } else {
            implCls = Instance.clazz(clsStr, defaultValue);
        }
        if (Objects.isNull(implCls)) {
            return null;
        }
        if (Instance.isMatch(implCls, interfaceCls)) {
            return implCls;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    static <T> T vValue(final JsonObject item, final String field, final Class<T> clazz) {
        if (Ut.isNil(item)) {
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

    // ------------------------------ 带映射专用方法 ----------------------------
    /*
     * mapping: from = to
     * 1. bMapping.keys() -> froms
     * 2. bMapping.to(String) -> from -> to
     */
    static JsonObject vTo(final JsonObject input, final BMapping mapping) {
        final JsonObject outputJ = new JsonObject();
        if (Objects.isNull(input)) {
            return outputJ;
        }
        mapping.keys().forEach(from -> {
            final Object value = input.getValue(from);
            final String to = mapping.to(from);
            outputJ.put(to, value);
        });
        return outputJ;
    }

    static JsonArray vTo(final JsonArray input, final BMapping mapping) {
        final JsonArray outputA = new JsonArray();
        Ut.itJArray(input).forEach(json -> outputA.add(vTo(json, mapping)));
        return outputA;
    }

    static JsonObject vTo(final JsonObject input, final JsonObject mapping, final boolean smart) {
        final BMapping bMapping = bMapping(mapping, smart);
        return vTo(input, bMapping);
    }

    static JsonArray vTo(final JsonArray input, final JsonObject mapping, final boolean smart) {
        return vTo(input, mapping, smart, null);
    }

    static JsonArray vTo(final JsonArray input, final JsonObject mapping, final boolean smart,
                         final BinaryOperator<JsonObject> itFn) {
        final BMapping bMapping = bMapping(mapping, smart);
        final JsonArray outputA = new JsonArray();
        Ut.itJArray(input).forEach(json -> {
            final JsonObject convert = vTo(json, bMapping);
            if (Objects.isNull(itFn)) {
                outputA.add(convert);
            } else {
                outputA.add(itFn.apply(convert, json));
            }
        });
        return outputA;
    }

    static JsonObject vFrom(final JsonObject input, final JsonObject mapping, final boolean smart) {
        final BMapping bMapping = bMapping(mapping, smart);
        return vFrom(input, bMapping);
    }

    static JsonArray vFrom(final JsonArray input, final JsonObject mapping, final boolean smart) {
        return vFrom(input, mapping, smart, null);
    }

    static JsonArray vFrom(final JsonArray input, final JsonObject mapping, final boolean smart,
                           final BinaryOperator<JsonObject> itFn) {
        final BMapping bMapping = bMapping(mapping, smart);
        final JsonArray outputA = new JsonArray();
        Ut.itJArray(input).forEach(json -> {
            final JsonObject convert = vFrom(json, bMapping);
            if (Objects.isNull(itFn)) {
                outputA.add(convert);
            } else {
                outputA.add(itFn.apply(convert, json));
            }
        });
        return outputA;
    }

    private static JsonObject vFrom(final JsonObject input, final BMapping mapping) {
        final JsonObject outputJ = new JsonObject();
        if (Objects.isNull(input)) {
            return outputJ;
        }
        mapping.values().forEach(to -> {
            final Object value = input.getValue(to);
            final String from = mapping.from(to);
            outputJ.put(from, value);
        });
        return outputJ;
    }

    private static JsonArray vFrom(final JsonArray input, final BMapping mapping) {
        final JsonArray outputA = new JsonArray();
        Ut.itJArray(input).forEach(json -> outputA.add(vFrom(json, mapping)));
        return outputA;
    }

    private static BMapping bMapping(final JsonObject mapping, final boolean smart) {
        final BMapping bMapping;
        if (smart) {
            final Object value = mapping.getValue(KName.MAPPING);
            if (value instanceof JsonObject stored) {
                bMapping = new BMapping(stored);
            } else {
                bMapping = new BMapping(mapping);
            }
        } else {
            bMapping = new BMapping(mapping);
        }
        return bMapping;
    }
}
