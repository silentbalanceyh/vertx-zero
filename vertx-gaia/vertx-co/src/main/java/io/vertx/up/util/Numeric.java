package io.vertx.up.util;

import io.horizon.eon.VString;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Number checking
 */
final class Numeric {
    private static final Annal LOGGER = Annal.get(Numeric.class);

    private Numeric() {
    }

    static Integer mathMultiply(final Integer left, final Integer right) {
        final Integer leftValue = Fn.orNull(0, () -> left, left);
        final Integer rightValue = Fn.orNull(0, () -> right, right);
        return Math.multiplyExact(leftValue, rightValue);
    }

    @SuppressWarnings("unchecked")
    static <T> T mathJSum(final JsonArray source, final String field, final Class<T> clazz) {
        return Fn.orNull(null, () -> {
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

    static boolean isPositive(final String original) {
        return StringUtil.isMatch(VString.REGEX.POSITIVE, original);
    }

    static boolean isNegative(final String original) {
        return StringUtil.isMatch(VString.REGEX.NEGATIVE, original);
    }

    static boolean isInteger(final String original) {
        return StringUtil.isMatch(VString.REGEX.INTEGER, original) || isPositive(original) || isNegative(original);
    }

    static Integer randomNumber(final int length) {
        // 1. Generate seed
        final StringBuilder min = new StringBuilder();
        final StringBuilder max = new StringBuilder();
        // 2. Calculate
        min.append(1);
        for (int idx = 0; idx < length; idx++) {
            min.append(0);
            max.append(9);
        }
        // 3. min/max
        final int minValue = Integer.parseInt(min.toString()) / 10;
        final int maxValue = Integer.parseInt(max.toString());
        final Random random = new Random();
        return minValue + random.nextInt(maxValue - minValue);
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
