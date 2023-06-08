package io.horizon.util;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author lang : 2023/4/27
 */
class _From extends _Error {
    protected _From() {
    }

    /**
     * 针对 LocalDate 类型的日期执行格式化
     *
     * @param date    LocalDate
     * @param pattern 时间格式模式
     *
     * @return 格式化后的字符串
     */
    public static String fromDate(final LocalDate date, final String pattern) {
        return TPeriod.fromPattern(date, pattern);
    }

    /**
     * 针对 LocalDateTime 类型的日期执行格式化
     *
     * @param datetime LocalDateTime
     * @param pattern  时间格式模式
     *
     * @return 格式化后的字符串
     */
    public static String fromDate(final LocalDateTime datetime, final String pattern) {
        return TPeriod.fromPattern(datetime, pattern);
    }

    /**
     * 针对 LocalTime 类型的日期执行格式化
     *
     * @param time    LocalTime
     * @param pattern 时间格式模式
     *
     * @return 格式化后的字符串
     */
    public static String fromDate(final LocalTime time, final String pattern) {
        return TPeriod.fromPattern(time, pattern);
    }

    /**
     * 针对 Date 类型的日期执行格式化
     *
     * @param date    Date
     * @param pattern 时间格式模式
     *
     * @return 格式化后的字符串
     */
    public static String fromDate(final Date date, final String pattern) {
        return TPeriod.fromPattern(TPeriod.toDateTime(date), pattern);
    }

    /**
     * 针对 Instant 类型的日期执行格式化
     *
     * @param instant Instant
     * @param pattern 时间格式模式
     *
     * @return 格式化后的字符串
     */
    public static String fromDate(final Instant instant, final String pattern) {
        return TPeriod.fromPattern(TPeriod.toDateTime(instant), pattern);
    }

    /**
     * 针对单个字符串或数值使用 fill 的字符进行左补充
     * 如：1 -> 0001
     *
     * @param seed  字符串
     * @param width 补充后的长度
     * @param fill  补充的字符
     *
     * @return 补充后的字符串
     */
    public static String fromAdjust(final Integer seed, final Integer width, final char fill) {
        return TString.fromAdjust(String.valueOf(seed), width, fill);
    }

    /**
     * 针对单个字符串或数值使用 fill 的字符进行左补充
     * 如：1 -> 0001
     *
     * @param seed  数值
     * @param width 补充后的长度
     * @param fill  补充的字符
     *
     * @return 补充后的字符串
     */
    public static String fromAdjust(final String seed, final Integer width, final char fill) {
        return TString.fromAdjust(seed, width, fill);
    }

    /**
     * 针对单个字符串或数值使用空格进行左补充
     *
     * @param seed  字符串
     * @param width 补充后的长度
     *
     * @return 补充后的字符串
     */
    public static String fromAdjust(final String seed, final Integer width) {
        return TString.fromAdjust(seed, width, ' ');
    }

    /**
     * 针对单个字符串或数值使用空格进行左补充
     *
     * @param seed  数值
     * @param width 补充后的长度
     *
     * @return 补充后的字符串
     */
    public static String fromAdjust(final Integer seed, final Integer width) {
        return TString.fromAdjust(String.valueOf(seed), width, '0');
    }

    /**
     * 从二进制Buffer中还原反序列化对象
     *
     * @param pos    位置
     * @param buffer 二进制Buffer
     * @param <T>    泛型
     *
     * @return 反序列化对象
     */
    public static <T> T fromBuffer(final int pos, final Buffer buffer) {
        return IoStream.from(pos, buffer);
    }

    /**
     * 将集合转换成字符串，默认使用逗号分隔符
     *
     * @param input 集合
     *
     * @return 字符串
     */
    public static String fromJoin(final Set<String> input) {
        return TString.join(input, null);
    }

    /**
     * 将集合转换成字符串
     *
     * @param input     集合
     * @param separator 分隔符
     *
     * @return 字符串
     */
    public static String fromJoin(final Set<String> input, final String separator) {
        return TString.join(input, separator);
    }

    /**
     * 将集合转换成字符串
     *
     * @param input     集合
     * @param separator 分隔符
     *
     * @return 字符串
     */
    public static String fromJoin(final List<String> input, final String separator) {
        return TString.join(input, separator);
    }

    /**
     * 将集合转换成字符串，默认使用逗号分隔符
     *
     * @param input 集合
     *
     * @return 字符串
     */
    public static String fromJoin(final List<String> input) {
        return TString.join(input, null);
    }

    /**
     * 将数组转换成字符串，默认使用逗号分隔符
     *
     * @param input 数组
     *
     * @return 字符串
     */
    public static String fromJoin(final Object[] input) {
        return fromJoin(input, null);
    }

    /**
     * 将数组转换成字符串
     *
     * @param input     数组
     * @param separator 分隔符
     *
     * @return 字符串
     */
    public static String fromJoin(final Object[] input, final String separator) {
        return TString.join(input, separator);
    }

    /**
     * 解析表达式转换，转换成想要的类型，参数使用 params 模式传入
     * 表达式如：`Hello ${name}` 这种格式
     *
     * @param expr   表达式
     * @param params 参数
     * @param <T>    泛型
     *
     * @return 转换后的对象
     */
    public static <T> T fromExpressionT(final String expr, final JsonObject params) {
        return HExpression.expressionT(expr, params);
    }

    /**
     * 解析表达式转换，转换成想要的类型，参数使用 params 模式传入
     * 表达式如：`Hello ${name}` 这种格式
     *
     * @param expr   表达式
     * @param params 参数
     *
     * @return 转换后的字符串
     */
    public static String fromExpression(final String expr, final JsonObject params) {
        return (String) HExpression.expressionWith(expr, params);
    }

    /**
     * 解析表达式转换（集合），转换成想要的类型，参数使用 params 模式传入
     * 表达式如：`Hello ${name}` 这种格式，递归转换所有格式
     *
     * @param exprObject 表达式
     * @param params     参数
     *
     * @return 转换后的字符串
     */
    public static JsonObject fromExpression(final JsonObject exprObject, final JsonObject params) {
        return HExpression.expression(exprObject, params);
    }

    /**
     * 解析表达式转换（集合），转换成想要的类型，参数使用 params 模式传入
     * 表达式如：`Hello ${name}` 这种格式，递归转换所有格式
     *
     * @param exprArray 表达式
     * @param params    参数
     *
     * @return 转换后的字符串
     */
    public static JsonArray fromExpression(final JsonArray exprArray, final JsonObject params) {
        return HExpression.expression(exprArray, params);
    }
}
