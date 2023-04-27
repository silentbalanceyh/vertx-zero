package io.horizon.util;

import java.util.Map;
import java.util.function.Supplier;

/**
 * High Metadata Service 高阶元数据工具类
 *
 * @author lang : 2023/4/27
 */
public final class HMs {

    private HMs() {
    }

    // ---------------- 格式化函数

    /**
     * 使用 MessageFormat 进行格式化，参数
     * {0} {1} {2}
     * 使用 Slf4j 进行格式化，参数
     * {} {} {}
     *
     * @param pattern 格式化模板
     * @param args    参数
     *
     * @return 格式化后的字符串
     */
    public static String fromMessage(final String pattern, final Object... args) {
        return HFormat.format(pattern, args);
    }

    // ---------------- 池化函数
    public static <V> V poolThread(final Map<String, V> pool, final Supplier<V> poolFn, final String key) {
        return HPool.poolThread(pool, poolFn, key);
    }

    public static <V> V poolThread(final Map<String, V> pool, final Supplier<V> poolFn) {
        return HPool.poolThread(pool, poolFn, null);
    }

    public static <K, V> V pool(final Map<K, V> pool, final K key, final Supplier<V> poolFn) {
        return HPool.pool(pool, key, poolFn);
    }

    // ---------------- 判断函数
    public static boolean isEmpty(final String input) {
        return HString.isEmpty(input);
    }

    public static boolean isNotEmpty(final String input) {
        return !isEmpty(input);
    }
}
