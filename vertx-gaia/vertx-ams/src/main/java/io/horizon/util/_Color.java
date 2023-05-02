package io.horizon.util;

/**
 * @author lang : 2023/4/28
 */
class _Color {
    protected _Color() {
    }

    /*
     * flag(N|B)(Color)       String.format （只支持单参）
     */
    // color = blank
    public static String rgbBlankB(final String pattern, final String flag) {
        return HRGB.color(pattern, flag, HRGB.COLOR_BLANK, true);
    }

    public static String rgbBlankB(final String flag) {
        return HRGB.color(null, flag, HRGB.COLOR_BLANK, true);
    }

    public static String rgbBlankN(final String pattern, final String flag) {
        return HRGB.color(pattern, flag, HRGB.COLOR_BLANK, false);
    }

    public static String rgbBlankN(final String flag) {
        return HRGB.color(null, flag, HRGB.COLOR_BLANK, false);
    }

    // color = blue
    public static String rgbBlueB(final String pattern, final String flag) {
        return HRGB.color(pattern, flag, HRGB.COLOR_BLUE, true);
    }

    public static String rgbBlueB(final String flag) {
        return HRGB.color(null, flag, HRGB.COLOR_BLUE, true);
    }

    public static String rgbBlueN(final String pattern, final String flag) {
        return HRGB.color(pattern, flag, HRGB.COLOR_BLUE, false);
    }

    public static String rgbBlueN(final String flag) {
        return HRGB.color(null, flag, HRGB.COLOR_BLUE, false);
    }

    // color = red
    public static String rgbRedB(final String pattern, final String flag) {
        return HRGB.color(pattern, flag, HRGB.COLOR_RED, true);
    }

    public static String rgbRedB(final String flag) {
        return HRGB.color(null, flag, HRGB.COLOR_RED, true);
    }

    public static String rgbRedN(final String pattern, final String flag) {
        return HRGB.color(pattern, flag, HRGB.COLOR_RED, false);
    }

    public static String rgbRedN(final String flag) {
        return HRGB.color(null, flag, HRGB.COLOR_RED, false);
    }

    // color = green

    public static String rgbGreenB(final String pattern, final String flag) {
        return HRGB.color(pattern, flag, HRGB.COLOR_GREEN, true);
    }

    public static String rgbGreenB(final String flag) {
        return HRGB.color(null, flag, HRGB.COLOR_GREEN, true);
    }

    public static String rgbGreenN(final String pattern, final String flag) {
        return HRGB.color(pattern, flag, HRGB.COLOR_GREEN, false);
    }

    public static String rgbGreenN(final String flag) {
        return HRGB.color(null, flag, HRGB.COLOR_GREEN, false);
    }

    // color = yellow

    public static String rgbYellowB(final String pattern, final String flag) {
        return HRGB.color(pattern, flag, HRGB.COLOR_YELLOW, true);
    }

    public static String rgbYellowB(final String flag) {
        return HRGB.color(null, flag, HRGB.COLOR_YELLOW, true);
    }

    public static String rgbYellowN(final String pattern, final String flag) {
        return HRGB.color(pattern, flag, HRGB.COLOR_YELLOW, false);
    }

    public static String rgbYellowN(final String flag) {
        return HRGB.color(null, flag, HRGB.COLOR_YELLOW, false);
    }

    // color = cyan

    public static String rgbCyanB(final String pattern, final String flag) {
        return HRGB.color(pattern, flag, HRGB.COLOR_CYAN, true);
    }

    public static String rgbCyanB(final String flag) {
        return HRGB.color(null, flag, HRGB.COLOR_CYAN, true);
    }

    public static String rgbCyanN(final String pattern, final String flag) {
        return HRGB.color(pattern, flag, HRGB.COLOR_CYAN, false);
    }

    public static String rgbCyanN(final String flag) {
        return HRGB.color(null, flag, HRGB.COLOR_CYAN, false);
    }

    // color = gray

    public static String rgbGrayB(final String pattern, final String flag) {
        return HRGB.color(pattern, flag, HRGB.COLOR_GRAY, true);
    }

    public static String rgbGrayB(final String flag) {
        return HRGB.color(null, flag, HRGB.COLOR_GRAY, true);
    }

    public static String rgbGrayN(final String pattern, final String flag) {
        return HRGB.color(pattern, flag, HRGB.COLOR_GRAY, false);
    }

    public static String rgbGrayN(final String flag) {
        return HRGB.color(null, flag, HRGB.COLOR_GRAY, false);
    }

}
