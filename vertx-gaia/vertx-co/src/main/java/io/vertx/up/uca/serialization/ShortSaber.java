package io.vertx.up.uca.serialization;

import java.util.function.Function;

/**
 * Short type
 */
@SuppressWarnings("unchecked")
public class ShortSaber extends NumericSaber {
    @Override
    protected boolean isValid(final Class<?> paramType) {
        return short.class == paramType || Short.class == paramType;
    }

    @Override
    protected Function<String, Short> getFun() {
        return Short::parseShort;
    }
}
