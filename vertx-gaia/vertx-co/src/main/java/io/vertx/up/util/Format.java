package io.vertx.up.util;

import org.slf4j.helpers.MessageFormatter;

import java.text.MessageFormat;

/**
 * @author lang : 2023/4/24
 */
class Format {

    /*
     * The color for console output
     */
    static final int COLOR_RED = 31;
    static final int COLOR_GREEN = 32;
    static final int COLOR_YELLOW = 33;
    static final int COLOR_BLUE = 34;
    static final int COLOR_CYAN = 36;
    static final int COLOR_GRAY = 37;
    static final int COLOR_BLANK = 30;
    private static final int NORMAL = 0;
    private static final int WEIGHT = 1;
    private static final String PREFIX = "\u001b[";
    private static final String SUFFIX = "m";
    private static final char SEPARATOR = ';';
    private static final String END_COLOUR = PREFIX + SUFFIX;
    private static final String BOLD_FLAG = PREFIX + WEIGHT + SEPARATOR + COLOR_BLANK + SUFFIX + "[ μηδέν ] " + END_COLOUR;

    private static String color(final String flag, final int color, final boolean bold) {
        final int weight = bold ? WEIGHT : NORMAL;
        return PREFIX + weight + SEPARATOR + color + SUFFIX + flag + END_COLOUR;
    }

    static String color(final String pattern, final String flag, final int color, final boolean bold) {
        if (StringUtil.isNil(pattern)) {
            return color(flag, color, bold);
        } else {
            return String.format(pattern, color(flag, color, bold));
        }
    }

    /**
     * 使用 MessageFormat 进行格式化，参数
     * {0} {1} {2}
     *
     * @param pattern 格式化模板
     * @param args    参数
     *
     * @return 格式化后的字符串
     */
    private static String format(final String pattern, final Object... args) {
        return MessageFormat.format(pattern, args);
    }

    static String formatBold(final String message, final Object... args) {
        if (0 < args.length) {
            return BOLD_FLAG + message(message, args);
        } else {
            return BOLD_FLAG + message;
        }
    }

    /**
     * 使用 Slf4j 进行格式化，参数
     * {} {} {}
     *
     * @param pattern 格式化模板
     * @param args    参数
     *
     * @return 格式化后的字符串
     */
    private static String formatter(final String pattern, final Object... args) {
        return MessageFormatter.format(pattern, args).getMessage();
    }

    static String message(final String pattern, final Object... args) {
        if (pattern.contains("{}")) {
            return formatter(pattern, args);
        } else {
            return format(pattern, args);
        }
    }
}
