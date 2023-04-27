package io.horizon.util;

import io.horizon.eon.runtime.VEnv;
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

    // ---------------- 判断函数：空/非空

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

    // ---------------- 判断函数：空/非空特殊

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


    // ---------------- 判断函数：特殊

    /**
     * 检查传入的字符串是否为UUID格式
     *
     * @param input 输入字符串
     *
     * @return 是否为UUID格式
     */
    public static boolean isUUID(final String input) {
        return HIs.isUUID(input);
    }

    /**
     * 对象比较函数，比较两个对象内容是否相等，带 null 检查的版本
     *
     * @param left  左对象
     * @param right 右对象
     *
     * @return 是否相等
     */
    public static boolean isEqual(final Object left, final Object right) {
        return HIs.isEqual(left, right);
    }

    /**
     * 对象比较函数，比较两个对象是否不等，带 null 检查的版本
     *
     * @param left  左对象
     * @param right 右对象
     *
     * @return 是否相等
     */
    public static boolean isDiff(final Object left, final Object right) {
        return !isEqual(left, right);
    }

    // ---------------- 判断函数：类型（反射）

    /**
     * 检查传入类型是否原始类型
     *
     * @param clazz 类型
     *
     * @return 是否原始类型
     */
    public static boolean isPrimary(final Class<?> clazz) {
        return VEnv.SPEC.TYPES.containsValue(clazz);
    }

    /**
     * 检查传入类型是否时间类型
     * 1. < 1.8 使用 Date.class 检查
     * 2. > 1.8 使用 Temporal.class 检查
     *
     * @param clazz 类型
     *
     * @return 是否时间类型
     */
    public static boolean isDate(final Class<?> clazz) {
        return HType.isDate(clazz);
    }


    /**
     * 检查传入类型是否布尔类型
     *
     * @param clazz 类型
     *
     * @return 是否布尔类型
     */
    public static boolean isBoolean(final Class<?> clazz) {
        return HType.isBoolean(clazz);
    }

    /**
     * 检查传入类型是否void类型
     *
     * @param clazz 类型
     *
     * @return 是否void类型
     */
    public static boolean isVoid(final Class<?> clazz) {
        return HType.isVoid(clazz);
    }

    /**
     * 检查传入类型是否整数类型
     *
     * @param clazz 类型
     *
     * @return 是否整数类型
     */
    public static boolean isInteger(final Class<?> clazz) {
        return HType.isInteger(clazz);
    }

    /**
     * 检查传入类型是否浮点数类型
     *
     * @param clazz 类型
     *
     * @return 是否浮点数类型
     */
    public static boolean isDecimal(final Class<?> clazz) {
        return HType.isDecimal(clazz);
    }

    /**
     * 检查传入是否数值类型
     *
     * @param clazz 类型
     *
     * @return 是否数值类型
     */
    public static boolean isNumber(final Class<?> clazz) {
        return HType.isNumber(clazz);
    }

    // ---------------- 判断函数：类型

    /**
     * （默认非宽松模式）检查传入字符串是否合法Boolean值
     *
     * @param literal 字符串
     *
     * @return 是否合法Boolean值
     */
    public static boolean isBoolean(final String literal) {
        return HIs.isBoolean(literal, false);
    }

    /**
     * 检查传入字符串是否合法Boolean值
     * 1. widely = true 时，支持 "true" / "false" / "1" / "0" / "yes" / "no" / "y" / "n"
     * 2. widely = false 时，支持 "true" / "false"
     *
     * @param literal 字符串
     * @param widely  是否宽松模式
     *
     * @return 是否合法Boolean值
     */
    public static boolean isBoolean(final String literal, final boolean widely) {
        return HIs.isBoolean(literal, widely);
    }

    // ---------------- Jvm强化函数
    public static void requireNonNull(final Object... args) {
        Arrays.stream(args).forEach(Objects::requireNonNull);
    }
}
