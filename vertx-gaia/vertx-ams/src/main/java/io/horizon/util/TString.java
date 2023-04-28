package io.horizon.util;

/**
 * @author lang : 2023/4/27
 */
class TString {
    private TString() {
    }

    private static String repeat(final Integer times, final char fill) {
        return String.valueOf(fill).repeat(Math.max(0, times));
    }

    static String fromAdjust(final String seed, final Integer width, final char fill) {
        final StringBuilder builder = new StringBuilder();
        final int seedLen = seed.length();
        int fillLen = width - seedLen;
        if (0 > fillLen) {
            fillLen = 0;
        }
        builder.append(repeat(fillLen, fill));
        builder.append(seed);
        return builder.toString();
    }
}
