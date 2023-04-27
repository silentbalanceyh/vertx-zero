package io.horizon.util;

import org.slf4j.helpers.MessageFormatter;

import java.text.MessageFormat;

/**
 * @author lang : 2023/4/27
 */
class HFormat {

    private static String formatJava(final String pattern, final Object... args) {
        return MessageFormat.format(pattern, args);
    }

    private static String formatSlf4j(final String pattern, final Object... args) {
        return MessageFormatter.format(pattern, args).getMessage();
    }

    static String format(final String pattern, final Object... args) {
        if (pattern.contains("{}")) {
            return formatSlf4j(pattern, args);
        } else {
            return formatJava(pattern, args);
        }
    }
}
