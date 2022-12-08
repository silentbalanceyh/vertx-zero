package io.vertx.up.uca.serialization;

import java.util.function.Function;

/**
 * Long type
 */
@SuppressWarnings("unchecked")
public class LongSaber extends NumericSaber {
    @Override
    protected boolean isValid(final Class<?> paramType) {
        return long.class == paramType || Long.class == paramType;
    }

    @Override
    protected Function<String, Long> getFun() {
        return Long::parseLong;
    }
}
