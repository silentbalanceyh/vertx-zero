package io.horizon.util;

import io.horizon.eon.VString;

import java.util.Objects;

/**
 * @author lang : 2023/4/27
 */
final class TNumeric {
    private TNumeric() {
    }

    static boolean isPositive(final String literal) {
        return Objects.nonNull(literal)
            && TIs.isMatch(literal, VString.REGEX.POSITIVE);
    }

    static boolean isPositive(final int number) {
        return 0 < number;
    }

    static boolean isNegative(final String literal) {
        return Objects.nonNull(literal)
            && TIs.isMatch(literal, VString.REGEX.NEGATIVE);
    }

    static boolean isNegative(final int number) {
        return 0 > number;
    }

    static boolean isInteger(final String literal) {
        return (Objects.nonNull(literal)
            && TIs.isMatch(literal, VString.REGEX.INTEGER))
            || isPositive(literal)
            || isNegative(literal);
    }

    static boolean isDecimal(final String literal) {
        return Objects.nonNull(literal)
            && TIs.isMatch(literal, VString.REGEX.DECIMAL);
    }

    static boolean isReal(final String literal) {
        return isInteger(literal) || isDecimal(literal);
    }

    static boolean isDecimalPositive(final String literal) {
        return Objects.nonNull(literal)
            && TIs.isMatch(literal, VString.REGEX.DECIMAL_POSITIVE);
    }

    static boolean isDecimalNegative(final String literal) {
        return Objects.nonNull(literal)
            && TIs.isMatch(literal, VString.REGEX.DECIMAL_NEGATIVE);
    }

    static boolean isIn(final Integer value, final Integer min, final Integer max) {
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
