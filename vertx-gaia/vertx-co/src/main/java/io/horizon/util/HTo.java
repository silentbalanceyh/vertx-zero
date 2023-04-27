package io.horizon.util;

import io.horizon.eon.runtime.VEnv;

import java.util.Objects;

/**
 * @author lang : 2023/4/27
 */
final class HTo {
    private HTo() {
    }

    static Class<?> toPrimary(final Class<?> source) {
        return VEnv.SPEC.TYPES.getOrDefault(source, source);
    }

    static <T extends Enum<T>> T toEnum(final String literal, final Class<T> clazz, final T defaultEnum) {
        if (HIs.isNil(literal) || Objects.isNull(clazz)) {
            return defaultEnum;
        }
        return Enum.valueOf(clazz, literal);
    }
}
