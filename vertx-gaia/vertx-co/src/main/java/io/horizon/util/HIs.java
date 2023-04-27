package io.horizon.util;

import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lang : 2023/4/27
 */
class HIs {
    static boolean isNil(final String str) {
        return Objects.isNull(str) || str.trim().isEmpty();
    }

    static boolean isNil(final JsonObject inputJ) {
        return Objects.isNull(inputJ) || inputJ.isEmpty();
    }

    static boolean isNil(final JsonArray inputA) {
        return Objects.isNull(inputA) || inputA.isEmpty();
    }

    static boolean isUUID(final String literal) {
        UUID converted;
        try {
            converted = UUID.fromString(literal);
        } catch (final IllegalArgumentException ex) {
            converted = null;
        }
        return Objects.nonNull(converted);
    }

    static boolean isSame(final Object left, final Object right) {
        if (Objects.isNull(left) && Objects.isNull(right)) {
            return true;
        } else {
            return Objects.nonNull(left) && Objects.nonNull(right) && left.equals(right);
        }
    }

    static boolean isBoolean(final String literal, final boolean widely) {
        if (Objects.isNull(literal)) {
            return false;
        } else {
            final String lower = literal.toLowerCase().trim();
            if (widely) {
                /*
                 * 匹配对
                 * yes / no
                 * true / false
                 * y / n
                 * 1 / 0
                 */
                return VValue.TRUE.equals(lower)
                    || VValue.FALSE.equals(lower)
                    || "yes".equals(lower)
                    || "no".equals(lower)
                    || "y".equals(lower)
                    || "n".equals(lower)
                    || "1".equals(lower)
                    || "0".equals(lower);
            } else {
                return VValue.TRUE.equals(lower)
                    || VValue.FALSE.equals(lower);
            }
        }
    }

    static boolean isMatch(final String value, final String regex) {
        return Fn.orNull(() -> {
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        }, regex, value);
    }


    static boolean isFileName(final String literal) {
        return Objects.nonNull(literal)
            && isMatch(literal, VString.REGEX.FILENAME);
    }

    static boolean isDate(final Object value) {
        if (Objects.isNull(value)) {
            return false;
        }

        if (value instanceof Class<?>) {
            return HType.isDate((Class<?>) value);
        } else {
            return HPeriod.isValid(value.toString());
        }
    }
}
