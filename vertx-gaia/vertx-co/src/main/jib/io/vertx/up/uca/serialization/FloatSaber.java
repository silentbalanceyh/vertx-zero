package io.vertx.up.uca.serialization;

import java.util.function.Function;

/**
 * Float type
 */
@SuppressWarnings("unchecked")
public class FloatSaber extends DecimalSaber {
    @Override
    protected boolean isValid(final Class<?> paramType) {
        return float.class == paramType || Float.class == paramType;
    }

    @Override
    protected Function<String, Float> getFun() {
        return Float::parseFloat;
    }
}
