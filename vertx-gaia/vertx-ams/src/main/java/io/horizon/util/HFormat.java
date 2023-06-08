package io.horizon.util;

import org.slf4j.helpers.MessageFormatter;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author lang : 2023/4/27
 */
class HFormat {
    private static final LogUtil LOG = LogUtil.from(HFormat.class);

    private static String formatJava(final String pattern, final Object... args) {
        return MessageFormat.format(pattern, args);
    }

    private static String formatSlf4j(final String pattern, final Object... args) {
        return MessageFormatter.arrayFormat(pattern, args).getMessage();
    }

    static String fromMessage(final String pattern, final Object... args) {
        if (pattern.contains("{}")) {
            return formatSlf4j(pattern, args);
        } else {
            return formatJava(pattern, args);
        }
    }

    static String fromResource(final ResourceBundle bundle,
                               final String key, final Object... args) {
        Objects.requireNonNull(bundle);
        try {
            final String pattern = bundle.getString(key);
            return fromMessage(pattern, args);
        } catch (final MissingResourceException ex) {
            LOG.error(ex.getMessage());
            throw ex;
        }
    }


    static String fromMessageB(final String message, final Object... args) {
        if (0 < args.length) {
            return HRGB.BOLD_FLAG + fromMessage(message, args);
        } else {
            return HRGB.BOLD_FLAG + message;
        }
    }
}
