package io.horizon.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * High Metadata Service 高阶元数据工具类
 *
 * @author lang : 2023/4/27
 */
public final class HH {

    private HH() {
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

    /**
     * 检查传入的字符串是否空字符串
     * 1. 长度为0，返回 true
     * 2. 否则执行 trim，调用 isEmpty 检查
     *
     * @param input 输入字符串
     *
     * @return 是否空字符串
     */
    public static boolean isNil(final String input) {
        return HIs.isNil(input);
    }

    /**
     * isNil重载版本，检查是否空Json对象
     *
     * @param input 输入Json对象
     *
     * @return 是否空Json对象
     */
    public static boolean isNil(final JsonObject input) {
        return HIs.isNil(input);
    }

    /**
     * isNil重载版本，检查是否空Json数组
     *
     * @param input 输入Json数组
     *
     * @return 是否空Json数组
     */
    public static boolean isNil(final JsonArray input) {
        return HIs.isNil(input);
    }

    /**
     * isNil的逆函数，检查是否非空字符串
     *
     * @param input 输入字符串
     *
     * @return 是否非空字符串
     */
    public static boolean isNotNil(final String input) {
        return !isNil(input);
    }

    /**
     * isNil的逆函数，检查是否非空Json对象
     *
     * @param input 输入Json对象
     *
     * @return 是否非空Json对象
     */
    public static boolean isNotNil(final JsonObject input) {
        return !isNil(input);
    }

    /**
     * isNil的逆函数，检查是否非空Json数组
     *
     * @param input 输入Json数组
     *
     * @return 是否非空Json数组
     */
    public static boolean isNotNil(final JsonArray input) {
        return !isNil(input);
    }

    /**
     * 「动参版本」
     * isNil的字符串特殊版本
     *
     * @param args 字符串数组
     *
     * @return 是否全部为空字符串
     */
    public static boolean isNil(final String... args) {
        return 0 == args.length || Arrays.stream(args).allMatch(HIs::isNil);
    }

    /**
     * 「动参版本」
     * isNil的Json对象特殊版本
     *
     * @param args Json对象数组
     *
     * @return 是否全部为空Json对象
     */
    public static boolean isNotNil(final String... args) {
        return 0 != args.length && Arrays.stream(args).noneMatch(HIs::isNil);
    }

    /**
     * 检查传入的对象是全部为null
     * 1. 长度为0时返回 true
     * 2. 否则调用 Arrays.stream(args).allMatch(Objects::isNull)
     *
     * @param args 对象数组
     *
     * @return 是否全部为null
     */
    public static boolean isNull(final Object... args) {
        return HIs.isNull(args);
    }

    /**
     * 检查传入的对象是全部为非null（参数检查可用）
     * 1. 长度为0时返回 true
     * 2. 否则调用 Arrays.stream(args).allMatch(Objects::nonNull)
     *
     * @param args 对象数组
     *
     * @return 是否全部为非null
     */
    public static boolean isNotNil(final Object... args) {
        return HIs.isNotNull(args);
    }

    public static void requireNonNull(final Object... args) {
        Arrays.stream(args).forEach(Objects::requireNonNull);
    }
}
