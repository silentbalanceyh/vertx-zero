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
 * 1）动参版本
 * 2）对象版本
 * 上述两个版本直接在 HH 类中铺开写逻辑
 * 包域中只包含基础逻辑，没有其他内容
 *
 * @author lang : 2023/4/27
 */
public class HH {
    /*
     * 构造函数只能被子类调用，不可以在其他地方调用，所以此处是工具类的重新设计，原理
     * 1. Java 中的静态方法可以从父类继承，但是不可以从接口初获得
     * 2. 虽然继承之后子类定义的方法只是隐藏了父类的，但不会出现Overwrite
     * 3. 但从代码去重上是一个非常好的策略
     *
     * 父类：protected 的默认构造函数 + 静态类
     * 子类：private 的构造函数 + final 不可继承类
     * 这种类最终会导致二者合并形成统一方法区间
     */
    protected HH() {
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

    // ---------------- 判断函数：空/非空（动参版）

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
     * 「动参版本」
     * 检查传入的对象是全部为null
     * 1. 长度为0时返回 true
     * 2. 否则调用 Arrays.stream(args).allMatch(Objects::isNull)
     *
     * @param args 对象数组
     *
     * @return 是否全部为null
     */
    public static boolean isNull(final Object... args) {
        return 0 == args.length || Arrays.stream(args)
            .allMatch(Objects::isNull);
    }

    /**
     * 「动参版本」
     * 检查传入的对象是全部为非null（参数检查可用）
     * 1. 长度为0时返回 true
     * 2. 否则调用 Arrays.stream(args).allMatch(Objects::nonNull)
     *
     * @param args 对象数组
     *
     * @return 是否全部为非null
     */
    public static boolean isNotNil(final Object... args) {
        return 0 == args.length || Arrays.stream(args)
            .noneMatch(Objects::isNull);
    }

    /**
     * 「动参版本」
     * 检查传入的对象是否全部是正整数
     *
     * @param numbers 对象数组（基础类型）
     *
     * @return 是否全部是正整数
     */
    public static boolean isPositive(final int... numbers) {
        return Arrays.stream(numbers)
            .allMatch(HNumeric::isPositive);
    }

    /**
     * 「动参版本」
     * 检查传入的对象是否全部是正整数
     *
     * @param numbers 对象数组
     *
     * @return 是否全部是正整数
     */
    public static boolean isPositive(final Integer... numbers) {
        return Arrays.stream(numbers)
            .allMatch(item -> Objects.nonNull(item) && HNumeric.isPositive(item));
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
     * 扩展参数模式，isBoolean 可检查 Object 类型，去空
     *
     * @param input 输入对象
     *
     * @return 是否合法Boolean值
     */
    public static boolean isBoolean(final Object input) {
        return Objects.nonNull(input)
            && isBoolean(input.toString().trim().intern());
    }

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

    /**
     * 检查一个字符串是否正整数，正则表达式模式
     *
     * @param literal 字符串
     *
     * @return 是否正整数
     */
    public static boolean isPositive(final String literal) {
        return HNumeric.isPositive(literal);
    }

    /**
     * （传函数）函数模式专用，检查一个数值是否正整数
     *
     * @param number 数值
     *
     * @return 是否正整数
     */
    public static boolean isPositive(final int number) {
        return HNumeric.isPositive(number);
    }

    /**
     * 检查一个对象是否正整数，非空场景中转换成 String 检查
     *
     * @param input 对象
     *
     * @return 是否正整数
     */
    public static boolean isPositive(final Object input) {
        return Objects.nonNull(input)
            && HNumeric.isPositive(input.toString().trim().intern());
    }

    /**
     * 检查一个字符串是否负整数，正则表达式模式
     *
     * @param literal 字符串
     *
     * @return 是否负整数
     */
    public static boolean isNegative(final String literal) {
        return HNumeric.isNegative(literal);
    }

    /**
     * （传函数）函数模式专用，检查一个数值是否负整数
     *
     * @param number 数值
     *
     * @return 是否负整数
     */
    public static boolean isNegative(final int number) {
        return HNumeric.isNegative(number);
    }

    /**
     * 检查一个对象是否负整数，非空场景中转换成 String 检查
     *
     * @param input 对象
     *
     * @return 是否负整数
     */
    public static boolean isNegative(final Object input) {
        return Objects.nonNull(input)
            && HNumeric.isNegative(input.toString().trim().intern());
    }

    /**
     * 检查一个字符串是否整数，正则表达式模式
     *
     * @param literal 字符串
     *
     * @return 是否整数
     */
    public static boolean isInteger(final String literal) {
        return HNumeric.isInteger(literal);
    }

    /**
     * 检查一个对象是否整数，非空场景中转换成 String 检查
     *
     * @param input 对象
     *
     * @return 是否整数
     */
    public static boolean isInteger(final Object input) {
        return Objects.nonNull(input)
            && HNumeric.isInteger(input.toString().trim().intern());
    }


    /**
     * 检查一个字符串是否实数，正则表达式模式
     *
     * @param literal 字符串
     *
     * @return 是否数值
     */
    public static boolean isReal(final String literal) {
        return HNumeric.isReal(literal);
    }

    /**
     * 检查一个对象是否实数，非空场景中转换成 String 检查
     *
     * @param input 对象
     *
     * @return 是否数值
     */
    public static boolean isReal(final Object input) {
        return Objects.nonNull(input)
            && HNumeric.isReal(input.toString().trim().intern());
    }

    /**
     * 检查一个字符串是否浮点数，正则表达式模式
     *
     * @param literal 字符串
     *
     * @return 是否浮点数
     */
    public static boolean isDecimal(final String literal) {
        return HNumeric.isDecimal(literal);
    }

    /**
     * 检查一个对象是否浮点数，非空场景中转换成 String 检查
     *
     * @param input 对象
     *
     * @return 是否浮点数
     */
    public static boolean isDecimal(final Object input) {
        return Objects.nonNull(input)
            && HNumeric.isDecimal(input.toString().trim().intern());
    }

    /**
     * 检查一个字符串是否正浮点数，正则表达式模式
     *
     * @param literal 字符串
     *
     * @return 是否正浮点数
     */
    public static boolean isDecimalPositive(final String literal) {
        return HNumeric.isDecimalPositive(literal);
    }

    /**
     * 检查一个对象是否正浮点数，非空场景中转换成 String 检查
     *
     * @param input 对象
     *
     * @return 是否正浮点数
     */
    public static boolean isDecimalPositive(final Object input) {
        return Objects.nonNull(input)
            && HNumeric.isDecimalPositive(input.toString().trim().intern());
    }

    /**
     * 检查一个字符串是否负浮点数，正则表达式模式
     *
     * @param literal 字符串
     *
     * @return 是否负浮点数
     */
    public static boolean isDecimalNegative(final String literal) {
        return HNumeric.isDecimalNegative(literal);
    }

    /**
     * 检查一个对象是否负浮点数，非空场景中转换成 String 检查
     *
     * @param input 对象
     *
     * @return 是否负浮点数
     */
    public static boolean isDecimalNegative(final Object input) {
        return Objects.nonNull(input)
            && HNumeric.isDecimalNegative(input.toString().trim().intern());
    }

    // ---------------- Jvm强化函数
    public static void requireNonNull(final Object... args) {
        Arrays.stream(args).forEach(Objects::requireNonNull);
    }
}
