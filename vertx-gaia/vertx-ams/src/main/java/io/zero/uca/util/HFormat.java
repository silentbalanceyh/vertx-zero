package io.zero.uca.util;

import org.slf4j.helpers.MessageFormatter;

import java.text.MessageFormat;

/**
 * @author lang : 2023/4/24
 */
class HFormat {

    static String format(final String pattern, final Object... args) {
        return MessageFormat.format(pattern, args);
    }

    static String formatter(final String pattern, final Object... args) {
        return MessageFormatter.format(pattern, args).getMessage();
    }
}
