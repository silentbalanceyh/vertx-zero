package io.horizon.util;

import io.horizon.eon.VString;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author lang : 2023/4/27
 */
class _To extends _Serialize {
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
        return TTo.toPrimary(source);
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
        return TTo.toEnum(literal, clazz, null);
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
        return TTo.toEnum(literal, clazz, defaultValue);
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
        return TTo.toEnum(supplier.get(), clazz, null);
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
        return TTo.toEnum(supplier.get(), clazz, defaultValue);
    }

    /**
     * 合法时间格式中字符串转成月
     *
     * @param literal 时间字符串
     *
     * @return 月份
     */
    public static int toMonth(final String literal) {
        return TPeriod.toMonth(literal);
    }

    /**
     * 提取时间中的月份
     *
     * @param date 时间
     *
     * @return 月份
     */
    public static int toMonth(final Date date) {
        return TPeriod.toMonth(date);
    }

    /**
     * 合法时间格式中字符串转成年
     *
     * @param literal 时间字符串
     *
     * @return 年份
     */
    public static int toYear(final String literal) {
        return TPeriod.toYear(literal);
    }

    /**
     * 提取时间中的年份
     *
     * @param date 时间
     *
     * @return 年份
     */
    public static int toYear(final Date date) {
        return TPeriod.toYear(date);
    }

    /**
     * 合法时间格式中字符串转成LocalDateTime
     *
     * @param literal 时间字符串
     *
     * @return LocalDateTime
     */

    public static LocalDateTime toDateTime(final String literal) {
        return TPeriod.toDateTime(literal);
    }

    /**
     * Date转换成LocalDateTime
     *
     * @param date Date
     *
     * @return LocalDateTime
     */
    public static LocalDateTime toDateTime(final Date date) {
        return TPeriod.toDateTime(date);
    }

    /**
     * Instant转换成LocalDateTime
     *
     * @param date Instant
     *
     * @return LocalDateTime
     */
    public static LocalDateTime toDateTime(final Instant date) {
        return TPeriod.toDateTime(date);
    }

    /**
     * 合法时间格式中字符串转成LocalDate
     *
     * @param literal 时间字符串
     *
     * @return LocalDate
     */
    public static LocalDate toDate(final String literal) {
        return TPeriod.toDate(literal);
    }

    /**
     * 合法时间格式中字符串转成LocalTime
     *
     * @param literal 时间字符串
     *
     * @return LocalTime
     */
    public static LocalTime toTime(final String literal) {
        return TPeriod.toTime(literal);
    }

    /**
     * 毫秒值转换成 LocalDateTime
     *
     * @param millSeconds 毫秒值
     *
     * @return LocalDateTime
     */
    public static LocalDateTime toDuration(final long millSeconds) {
        return TPeriod.toDuration(millSeconds);
    }

    /**
     * Date类型的时间转换成 LocalDate，Java 8+
     *
     * @param date Date
     *
     * @return LocalDate
     */
    public static LocalDate toDate(final Date date) {
        return TPeriod.toDate(date);
    }

    /**
     * Date类型的时间转换成 LocalTime，Java 8+
     *
     * @param date Date
     *
     * @return LocalTime
     */
    public static LocalTime toTime(final Date date) {
        return TPeriod.toTime(date);
    }

    /**
     * Instant类型的时间转换成 LocalDate，Java 8+
     *
     * @param date Instant
     *
     * @return LocalDate
     */
    public static LocalDate toDate(final Instant date) {
        return TPeriod.toDate(date);
    }

    /**
     * Instant类型的时间转换成 LocalTime，Java 8+
     *
     * @param date Instant
     *
     * @return LocalTime
     */
    public static LocalTime toTime(final Instant date) {
        return TPeriod.toTime(date);
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

    /**
     * 将 JsonArray 转换 Set<String>
     *
     * @param arrayA JsonArray
     *
     * @return Set<String>
     */
    public static Set<String> toSet(final JsonArray arrayA) {
        return new HashSet<>(TTo.toList(arrayA));
    }

    /**
     * 将 JsonArray 转换 List<String>
     *
     * @param arrayA JsonArray
     *
     * @return List<String>
     */
    public static List<String> toList(final JsonArray arrayA) {
        return TTo.toList(arrayA);
    }

    /**
     * 将 String 转换成 List<String>，默认逗号分割
     *
     * @param literal 字符串字面量
     *
     * @return List<String>
     */
    public static List<String> toList(final String literal) {
        return TString.split(literal, VString.COMMA);
    }

    /**
     * 将 String 转换成 List<String>，默认逗号分割
     *
     * @param literal   字符串字面量
     * @param separator 分隔符
     *
     * @return List<String>
     */
    public static List<String> toList(final String literal, final String separator) {
        return TString.split(literal, separator);
    }

    /**
     * 将 String 转换成 Set<String>，默认逗号分割
     *
     * @param literal 字符串字面量
     *
     * @return Set<String>
     */
    public static Set<String> toSet(final String literal) {
        return new HashSet<>(TString.split(literal, VString.COMMA));
    }

    /**
     * 将 String 转换成 Set<String>，默认逗号分割
     *
     * @param literal   字符串字面量
     * @param separator 分隔符
     *
     * @return Set<String>
     */
    public static Set<String> toSet(final String literal, final String separator) {
        return new HashSet<>(TString.split(literal, separator));
    }

    /**
     * 将字符串字面量转换成 JsonArray
     *
     * @param literal 字符串字面量
     *
     * @return JsonArray
     */
    public static JsonArray toJArray(final String literal) {
        return HJson.toJArray(literal);
    }

    /**
     * （非空检查）集合转换成 JsonArray
     *
     * @param set 集合
     * @param <T> 集合类型
     *
     * @return JsonArray
     */
    public static <T> JsonArray toJArray(final Set<T> set) {
        return HJson.toJArray(set);
    }

    /**
     * （非空检查）集合转换成 JsonArray
     *
     * @param list 集合
     * @param <T>  集合类型
     *
     * @return JsonArray
     */
    public static <T> JsonArray toJArray(final List<T> list) {
        return HJson.toJArray(list);
    }

    /**
     * 增强型转换，可注入转换函数执行附加操作构造新的 JsonArray
     *
     * @param literal 字符串字面量
     * @param itemFn  转换函数
     *
     * @return JsonArray
     */
    public static JsonArray toJArray(final String literal, final Function<JsonObject, JsonObject> itemFn) {
        return HJson.toJArray(literal, itemFn);
    }

    /**
     * 增强型转换，可注入转换函数执行附加操作构造新的 JsonArray
     *
     * @param array    JsonArray
     * @param executor 转换函数
     *
     * @return JsonArray
     */
    public static JsonArray toJArray(final JsonArray array, final Function<JsonObject, JsonObject> executor) {
        return HJson.toJArray(array, executor);
    }

    /**
     * 智能转换，对对象进行类型判断，转换成 JsonArray
     *
     * @param value 对象
     *
     * @return JsonArray
     */
    public static JsonArray toJArray(final Object value) {
        return HJson.toJArray(value);
    }


    /**
     * （带非空检查）Map转换成 JsonObject
     *
     * @param map Map
     *
     * @return JsonObject
     */
    public static JsonObject toJObject(final Map<String, Object> map) {
        return HJson.toJObject(map);
    }

    /**
     * 增强型转换，可注入转换函数执行附加操作构造新的 JsonObject
     *
     * @param literal 字符串字面量
     * @param itemFn  转换函数
     *
     * @return JsonObject
     */
    public static JsonObject toJObject(final String literal, final Function<JsonObject, JsonObject> itemFn) {
        return HJson.toJObject(literal, itemFn);
    }

    /**
     * 字符串转换成 JsonObject，如果有错默认返回空对象
     *
     * @param literal 字符串
     *
     * @return JsonObject
     */
    public static JsonObject toJObject(final String literal) {
        return HJson.toJObject(literal);
    }

    /**
     * 智能转换，对对象进行类型判断
     *
     * @param value 对象
     *
     * @return JsonObject
     */
    public static JsonObject toJObject(final Object value) {
        return HJson.toJObject(value);
    }

    /**
     * 智能转换，将对象转换成 String 类型
     *
     * @param value 对象
     *
     * @return String
     */
    public static String toString(final Object value) {
        return TTo.toString(value);
    }

    /**
     * 只能转换，将对象转换成 Collection 类型
     *
     * @param value 对象
     *
     * @return Collection
     */
    public static Collection<?> toCollection(final Object value) {
        return TTo.toCollection(value);
    }
}
