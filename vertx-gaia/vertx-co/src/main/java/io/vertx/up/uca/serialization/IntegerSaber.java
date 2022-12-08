package io.vertx.up.uca.serialization;

import java.util.function.Function;

/**
 * Integer type
 */
@SuppressWarnings("unchecked")
public class IntegerSaber extends NumericSaber {
    @Override
    protected boolean isValid(final Class<?> paramType) {
        return int.class == paramType || Integer.class == paramType;
    }

    @Override
    protected Function<String, Integer> getFun() {
        return Integer::parseInt;
    }
}
