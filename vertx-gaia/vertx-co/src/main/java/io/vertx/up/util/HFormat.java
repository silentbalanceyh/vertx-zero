package io.vertx.up.util;

import org.slf4j.helpers.MessageFormatter;

import java.text.MessageFormat;

/**
 * @author lang : 2023/4/24
 */
class HFormat {
    /**
     * 使用 MessageFormat 进行格式化，参数
     * {0} {1} {2}
     *
     * @param pattern 格式化模板
     * @param args    参数
     *
     * @return 格式化后的字符串
     */
    static String format(final String pattern, final Object... args) {
        return MessageFormat.format(pattern, args);
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
    static String formatter(final String pattern, final Object... args) {
        return MessageFormatter.format(pattern, args).getMessage();
    }
}
