package io.vertx.up.uca.serialization;

import java.util.function.Function;

/**
 * Double type
 */
@SuppressWarnings("unchecked")
public class DoubleSaber extends DecimalSaber {
    @Override
    protected boolean isValid(final Class<?> paramType) {
        return double.class == paramType || Double.class == paramType;
    }

    @Override
    protected Function<String, Double> getFun() {
        return Double::parseDouble;
    }
}
