package io.horizon.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author lang : 2023/4/27
 */
class _From extends _Element {
    /**
     * 针对 LocalDate 类型的日期执行格式化
     *
     * @param date    LocalDate
     * @param pattern 时间格式模式
     *
     * @return 格式化后的字符串
     */
    public static String fromDate(final LocalDate date, final String pattern) {
        return HPeriod.fromPattern(date, pattern);
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
        return HPeriod.fromPattern(datetime, pattern);
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
        return HPeriod.fromPattern(time, pattern);
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
        return HPeriod.fromPattern(HPeriod.toDateTime(date), pattern);
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
        return HPeriod.fromPattern(HPeriod.toDateTime(instant), pattern);
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
        return HString.fromAdjust(String.valueOf(seed), width, fill);
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
        return HString.fromAdjust(seed, width, fill);
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
        return HString.fromAdjust(seed, width, ' ');
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
        return HString.fromAdjust(String.valueOf(seed), width, '0');
    }
}
