package io.vertx.up.util;

import io.horizon.eon.VString;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

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

    @Deprecated
    static boolean isPositive(final String original) {
        return StringUtil.isMatch(VString.REGEX.POSITIVE, original);
    }

    @Deprecated
    static boolean isNegative(final String original) {
        return StringUtil.isMatch(VString.REGEX.NEGATIVE, original);
    }

    @Deprecated
    static boolean isInteger(final String original) {
        return StringUtil.isMatch(VString.REGEX.INTEGER, original) || isPositive(original) || isNegative(original);
    }

    static boolean isRange(final Integer value, final Integer min, final Integer max) {
        // min / max = null
        if (null == min && null == max) {
            return true;
        } else if (null != min && null != max) {
            return min <= value && value <= max;
        } else {
            return ((null != min) && min <= value) ||
                ((null != max) && value <= max);
        }
    }
}
