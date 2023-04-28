package io.horizon.util;

import io.horizon.eon.runtime.VEnv;
import io.horizon.fn.HFn;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;

/**
 * @author lang : 2023/4/27
 */
final class TTo {
    private TTo() {
    }

    static Class<?> toPrimary(final Class<?> source) {
        return VEnv.SPEC.TYPES.getOrDefault(source, source);
    }

    static <T extends Enum<T>> T toEnum(final String literal, final Class<T> clazz, final T defaultEnum) {
        if (TIs.isNil(literal) || Objects.isNull(clazz)) {
            return defaultEnum;
        }
        return Enum.valueOf(clazz, literal);
    }

    static List<String> toList(final JsonArray keys) {
        final JsonArray keysData = HaS.valueJArray(keys);
        final List<String> keyList = new ArrayList<>();
        HIter.itJString(keysData).forEach(keyList::add);
        return keyList;
    }

    static Collection<?> toCollection(final Object value) {
        return HFn.runOr(() -> {
            // Collection
            if (value instanceof Collection) {
                return ((Collection<?>) value);
            }
            // JsonArray
            if (HaS.isJArray(value)) {
                return ((JsonArray) value).getList();
            }
            // Object[]
            if (HaS.isArray(value)) {
                // Array
                final Object[] values = (Object[]) value;
                return Arrays.asList(values);
            }
            return null;
        }, value);
    }


    static String toString(final Object reference) {
        return HFn.runOr("null", () -> {
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
}
