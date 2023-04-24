package io.zero.uca.util;

/**
 * @author lang : 2023/4/24
 */
public final class HMS {
    private HMS() {
    }
    // ------------- 格式化API

    /**
     * 使用 MessageFormat 进行格式化，参数
     * {0} {1} {2}
     *
     * @param pattern 格式化模板
     * @param args    参数
     *
     * @return 格式化后的字符串
     */
    public static String messageJava(final String pattern, final Object... args) {
        return HFormat.format(pattern, args);
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
    public static String messageSlf4j(final String pattern, final Object... args) {
        return HFormat.formatter(pattern, args);
    }
}
