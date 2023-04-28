package io.horizon.util;

import io.vertx.core.buffer.Buffer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author lang : 2023/4/27
 */
class _To extends _Reflect {
    protected _To() {
    }

    /**
     * 根据传入类型将该类型转换成基本类型
     *
     * @param source 源类型
     *
     * @return 基本类型
     */
    public static Class<?> toPrimary(final Class<?> source) {
        return HTo.toPrimary(source);
    }

    /**
     * 根据传入枚举类型做字符串级别的转换
     *
     * @param clazz   枚举元类型
     * @param literal 字符串
     * @param <T>     枚举类型
     *
     * @return 枚举
     */
    public static <T extends Enum<T>> T toEnum(final String literal, final Class<T> clazz) {
        return HTo.toEnum(literal, clazz, null);
    }

    /**
     * 根据传入枚举类型做字符串级别的转换（带默认值）
     *
     * @param clazz        枚举元类型
     * @param literal      字符串
     * @param defaultValue 默认值
     * @param <T>          枚举类型
     *
     * @return 枚举
     */
    public static <T extends Enum<T>> T toEnum(final String literal, final Class<T> clazz, final T defaultValue) {
        return HTo.toEnum(literal, clazz, defaultValue);
    }

    /**
     * 根据传入枚举类型做字符串级别的转换（函数模式）
     *
     * @param supplier 字符串提供者
     * @param clazz    枚举元类型
     * @param <T>      枚举类型
     *
     * @return 枚举
     */
    public static <T extends Enum<T>> T toEnum(final Supplier<String> supplier, final Class<T> clazz) {
        return HTo.toEnum(supplier.get(), clazz, null);
    }

    /**
     * 根据传入枚举类型做字符串级别的转换（函数模式带默认值）
     *
     * @param supplier     字符串提供者
     * @param clazz        枚举元类型
     * @param defaultValue 默认值
     * @param <T>          枚举类型
     *
     * @return 枚举
     */
    public static <T extends Enum<T>> T toEnum(final Supplier<String> supplier, final Class<T> clazz, final T defaultValue) {
        return HTo.toEnum(supplier.get(), clazz, defaultValue);
    }

    /**
     * 合法时间格式中字符串转成月
     *
     * @param literal 时间字符串
     *
     * @return 月份
     */
    public static int toMonth(final String literal) {
        return HPeriod.toMonth(literal);
    }

    /**
     * 提取时间中的月份
     *
     * @param date 时间
     *
     * @return 月份
     */
    public static int toMonth(final Date date) {
        return HPeriod.toMonth(date);
    }

    /**
     * 合法时间格式中字符串转成年
     *
     * @param literal 时间字符串
     *
     * @return 年份
     */
    public static int toYear(final String literal) {
        return HPeriod.toYear(literal);
    }

    /**
     * 提取时间中的年份
     *
     * @param date 时间
     *
     * @return 年份
     */
    public static int toYear(final Date date) {
        return HPeriod.toYear(date);
    }

    /**
     * 合法时间格式中字符串转成LocalDateTime
     *
     * @param literal 时间字符串
     *
     * @return LocalDateTime
     */

    public static LocalDateTime toDateTime(final String literal) {
        return HPeriod.toDateTime(literal);
    }

    /**
     * Date转换成LocalDateTime
     *
     * @param date Date
     *
     * @return LocalDateTime
     */
    public static LocalDateTime toDateTime(final Date date) {
        return HPeriod.toDateTime(date);
    }

    /**
     * Instant转换成LocalDateTime
     *
     * @param date Instant
     *
     * @return LocalDateTime
     */
    public static LocalDateTime toDateTime(final Instant date) {
        return HPeriod.toDateTime(date);
    }

    /**
     * 合法时间格式中字符串转成LocalDate
     *
     * @param literal 时间字符串
     *
     * @return LocalDate
     */
    public static LocalDate toDate(final String literal) {
        return HPeriod.toDate(literal);
    }

    /**
     * 合法时间格式中字符串转成LocalTime
     *
     * @param literal 时间字符串
     *
     * @return LocalTime
     */
    public static LocalTime toTime(final String literal) {
        return HPeriod.toTime(literal);
    }

    /**
     * 毫秒值转换成 LocalDateTime
     *
     * @param millSeconds 毫秒值
     *
     * @return LocalDateTime
     */
    public static LocalDateTime toDuration(final long millSeconds) {
        return HPeriod.toDuration(millSeconds);
    }

    /**
     * Date类型的时间转换成 LocalDate，Java 8+
     *
     * @param date Date
     *
     * @return LocalDate
     */
    public static LocalDate toDate(final Date date) {
        return HPeriod.toDate(date);
    }

    /**
     * Date类型的时间转换成 LocalTime，Java 8+
     *
     * @param date Date
     *
     * @return LocalTime
     */
    public static LocalTime toTime(final Date date) {
        return HPeriod.toTime(date);
    }

    /**
     * Instant类型的时间转换成 LocalDate，Java 8+
     *
     * @param date Instant
     *
     * @return LocalDate
     */
    public static LocalDate toDate(final Instant date) {
        return HPeriod.toDate(date);
    }

    /**
     * Instant类型的时间转换成 LocalTime，Java 8+
     *
     * @param date Instant
     *
     * @return LocalTime
     */
    public static LocalTime toTime(final Instant date) {
        return HPeriod.toTime(date);
    }


    /**
     * 读取所有文件信息转换成压缩文件流
     *
     * @param fileSet 文件集合
     *
     * @return 压缩文件流
     */
    public static Buffer toZip(final Set<String> fileSet) {
        return Io.zip(fileSet);
    }

    /**
     * 将传入对象转换成字节数组
     *
     * @param message 对象
     * @param <T>     对象类型
     *
     * @return 字节数组
     */
    public static <T> byte[] toBytes(final T message) {
        return IoStream.to(message);
    }
}
