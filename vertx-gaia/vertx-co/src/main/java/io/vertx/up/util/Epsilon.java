package io.vertx.up.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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
        return (String) valueS;
    }

    static String vEnv(final String name, final String defaultValue) {
        final String parsed = System.getenv(name);
        return Ut.isNil(parsed) ? defaultValue : parsed;
    }

    static Class<?> vClass(final JsonObject json, final String field, final Class<?> defaultValue) {
        final String clsStr = vString(json, field, null);
        if (Ut.isNil(clsStr)) {
            return defaultValue;
        }
        return Instance.clazz(clsStr, defaultValue);
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
}
