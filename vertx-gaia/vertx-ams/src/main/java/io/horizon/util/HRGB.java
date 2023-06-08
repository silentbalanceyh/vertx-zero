package io.horizon.util;

/**
 * @author lang : 2023/4/24
 */
class HRGB {

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
    static final String BOLD_FLAG = PREFIX + WEIGHT + SEPARATOR + COLOR_BLANK + SUFFIX + "[ μηδέν ] " + END_COLOUR;

    private static String color(final String flag, final int color, final boolean bold) {
        final int weight = bold ? WEIGHT : NORMAL;
        return PREFIX + weight + SEPARATOR + color + SUFFIX + flag + END_COLOUR;
    }

    static String color(final String pattern, final String flag, final int color, final boolean bold) {
        if (HUt.isNil(pattern)) {
            return color(flag, color, bold);
        } else {
            return String.format(pattern, color(flag, color, bold));
        }
    }
}
