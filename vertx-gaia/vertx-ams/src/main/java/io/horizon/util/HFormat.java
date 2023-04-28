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

    static String fromMessage(final String pattern, final Object... args) {
        if (pattern.contains("{}")) {
            return formatSlf4j(pattern, args);
        } else {
            return formatJava(pattern, args);
        }
    }

    static String formatBold(final String message, final Object... args) {
        if (0 < args.length) {
            return HRGB.BOLD_FLAG + fromMessage(message, args);
        } else {
            return HRGB.BOLD_FLAG + message;
        }
    }
}
