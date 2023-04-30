package io.vertx.up.util;

import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;

import java.math.BigDecimal;

/**
 * Number checking
 */
final class Numeric {
    private static final Annal LOGGER = Annal.get(Numeric.class);

    private Numeric() {
    }

    static Integer mathMultiply(final Integer left, final Integer right) {
        final Integer leftValue = Fn.runOr(0, () -> left, left);
        final Integer rightValue = Fn.runOr(0, () -> right, right);
        return Math.multiplyExact(leftValue, rightValue);
    }

    @SuppressWarnings("unchecked")
    static <T> T mathJSum(final JsonArray source, final String field, final Class<T> clazz) {
        return Fn.runOr(null, () -> {
            Object returnValue = null;
            if (Double.class == clazz || BigDecimal.class == clazz) {
                final double result = source.stream().mapToDouble(item -> JsonObject.mapFrom(item).getDouble(field)).sum();
                returnValue = BigDecimal.class == clazz ? new BigDecimal(result) : result;
            } else if (Long.class == clazz) {
                returnValue = source.stream().mapToLong(item -> JsonObject.mapFrom(item).getLong(field)).sum();
            } else if (Integer.class == clazz) {
                returnValue = source.stream().mapToInt(item -> JsonObject.mapFrom(item).getInteger(field)).sum();
            } else {
                LOGGER.error(Info.MATH_NOT_MATCH, clazz);
            }
            return null == returnValue ? null : (T) returnValue;
        }, source, field, clazz);
    }
}
