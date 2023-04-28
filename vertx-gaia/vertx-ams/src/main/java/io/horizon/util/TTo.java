package io.horizon.util;

import io.horizon.eon.runtime.VEnv;
import io.vertx.core.json.JsonArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
}
