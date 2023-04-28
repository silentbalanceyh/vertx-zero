package io.horizon.util;

import io.horizon.eon.VValue;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @author lang : 2023/4/27
 */
class _Value extends _To {
    /**
     * （计算过时区）获取当前时间，转换成 Date 值
     *
     * @return Date
     */
    public static Date valueNow() {
        return TPeriod.parse(LocalDateTime.now());
    }

    /**
     * 返回安全的JsonObject对象，去掉了null pointer引起的各种问题，该版本中
     * 最终的结果一定会返回一个可操作的JsonObject对象。
     *
     * @param inputJ 输入的JsonObject对象
     *
     * @return 安全的JsonObject对象
     */
    public static JsonObject valueJObject(final JsonObject inputJ) {
        return HJson.valueJObject(inputJ, false);
    }

    /**
     * 返回安全的JsonArray对象，去掉了null pointer引起的各种问题，该版本中
     * 最终的结果一定会返回一个可操作的JsonArray对象。
     *
     * @param inputA 输入的JsonArray对象
     *
     * @return 安全的JsonArray对象
     */
    public static JsonArray valueJArray(final JsonArray inputA) {
        return HJson.valueJArray(inputA, false);
    }

    /**
     * 返回安全的JsonObject对象，去掉了null pointer引起的各种问题，该版本中
     * 最终的结果一定会返回一个可操作的JsonObject对象（返回副本）。
     *
     * @param inputJ 输入的JsonObject对象
     * @param isCopy 是否返回副本
     *
     * @return 安全的JsonObject对象或副本
     */
    public static JsonObject valueJObject(final JsonObject inputJ, final boolean isCopy) {
        return HJson.valueJObject(inputJ, isCopy);
    }

    /**
     * 返回安全的JsonArray对象，去掉了null pointer引起的各种问题，该版本中
     * 最终的结果一定会返回一个可操作的JsonArray对象（返回副本）。
     *
     * @param inputA 输入的JsonArray对象
     * @param isCopy 是否返回副本
     *
     * @return 安全的JsonArray对象或副本
     */

    public static JsonArray valueJArray(final JsonArray inputA, final boolean isCopy) {
        return HJson.valueJArray(inputA, isCopy);
    }

    /**
     * 从 JsonArray 中提取某个字段的属性值，并且转换成 JsonArray 集
     *
     * @param array JsonArray
     * @param field 字段名
     *
     * @return JsonArray
     */
    public static JsonArray valueJArray(final JsonArray array, final String field) {
        return HJson.toJArray(valueSetString(array, field));
    }

    /**
     * 从 JsonObject 中提取指定字段的值，如果该字段不存在或者值为null
     * 1. 为 null 返回 JsonObject
     * 2. 如果类型不匹配也返回 JsonObject
     *
     * @param inputJ JsonObject
     * @param field  字段名
     *
     * @return JsonObject
     */
    public static JsonObject valueJObject(final JsonObject inputJ, final String field) {
        return HJson.valueJObject(inputJ, field);
    }

    /**
     * 从 JsonObject 中提取指定字段的值，如果该字段不存在或者值为null
     * 1. 为 null 返回 JsonArray
     * 2. 如果类型不匹配也返回 JsonArray
     *
     * @param inputJ JsonObject
     * @param field  字段名
     *
     * @return JsonArray
     */
    public static JsonArray valueJArray(final JsonObject inputJ, final String field) {
        return HJson.valueJArray(inputJ, field);
    }

    /**
     * 映射计算，提取 JsonArray 中的制定字段的值，合并成一个 Set<String>
     *
     * @param array JsonArray
     * @param field 字段名
     *
     * @return Set<String>
     */
    public static Set<String> valueSetString(final JsonArray array, final String field) {
        return TValue.vStringSet(array, field);
    }

    /**
     * 映射计算，带提取函数，提取 List<T> 中的制定字段的值，合并成一个 Set<String>
     *
     * @param list     List<T>
     * @param executor 提取函数
     * @param <T>      T
     *
     * @return Set<String>
     */
    public static <T> Set<String> valueSetString(final List<T> list, final Function<T, String> executor) {
        return TValue.vStringSet(list, executor);
    }

    /**
     * （二维计算）映射计算，提取 JsonArray 中的制定字段的值，合并成一个 Set<JsonArray>
     *
     * @param array JsonArray
     * @param field 字段名
     *
     * @return Set<JsonArray>
     */
    public static Set<JsonArray> valueSetArray(final JsonArray array, final String field) {
        return TValue.vArraySet(array, field);
    }


    /**
     * 查找第一个匹配转换函数的字符串
     *
     * @param list     列表
     * @param stringFn 转换函数
     * @param <T>      T
     *
     * @return 字符串
     */
    public static <T> String valueFirst(final List<T> list, final Function<T, String> stringFn) {
        return list.stream().map(stringFn).findFirst().orElse(null);
    }

    /**
     * 提取某个属性（唯一字符串），如果不唯一则返回 null
     *
     * @param array JsonArray
     * @param field 字段名
     *
     * @return String
     */
    public static String valueString(final JsonArray array, final String field) {
        return TValue.vString(array, field);
    }

    /**
     * （带非空检查）提取 JsonObject 中某个 String 属性值
     *
     * @param json  JsonObject
     * @param field 字段名
     *
     * @return String
     */
    public static String valueString(final JsonObject json, final String field) {
        return TValue.vString(json, field, null);
    }

    /**
     * （带非空检查）提取 JsonObject 中某个 String 属性值
     *
     * @param json         JsonObject
     * @param field        字段名
     * @param defaultValue 默认值
     *
     * @return String
     */
    public static String valueString(final JsonObject json, final String field, final String defaultValue) {
        return TValue.vString(json, field, defaultValue);
    }

    /**
     * （带非空检查）提取 JsonObject 中某个 Integer 属性值
     *
     * @param json  JsonObject
     * @param field 字段名
     *
     * @return String
     */
    public static Integer valueInt(final JsonObject json, final String field) {
        return TValue.vInt(json, field, VValue.RANGE);
    }

    /**
     * （带非空检查）提取 JsonObject 中某个 String 属性值，并转换成 Class<?>
     *
     * @param json  JsonObject
     * @param field 字段名
     *
     * @return String
     */
    public static Class<?> valueC(final JsonObject json, final String field) {
        return TValue.vClass(json, field, null);
    }

    /**
     * （带非空检查）提取 JsonObject 中某个 String 属性值，并转换成 Class<?>
     *
     * @param json         JsonObject
     * @param field        字段名
     * @param defaultClass 默认值
     *
     * @return String
     */
    public static Class<?> valueC(final JsonObject json, final String field,
                                  final Class<?> defaultClass) {
        return TValue.vClass(json, field, defaultClass);
    }

    /**
     * （带非空检查）提取 JsonObject 中某个 String 属性值，并转换成 Class<?>，且该 Class<?>
     * 必须是实现了 interfaceCls 的
     *
     * @param json         JsonObject
     * @param field        字段名
     * @param interfaceCls 接口类
     *
     * @return String
     */
    public static Class<?> valueCI(final JsonObject json, final String field,
                                   final Class<?> interfaceCls) {
        return TValue.vClass(json, field, interfaceCls, null);
    }

    /**
     * （带非空检查）提取 JsonObject 中某个 String 属性值，并转换成 Class<?>，且该 Class<?>
     * 必须是实现了 interfaceCls 的
     *
     * @param json         JsonObject
     * @param field        字段名
     * @param interfaceCls 接口类
     * @param defaultClass 默认值
     *
     * @return String
     */
    public static Class<?> valueCI(final JsonObject json, final String field,
                                   final Class<?> interfaceCls, final Class<?> defaultClass) {
        return TValue.vClass(json, field, interfaceCls, defaultClass);
    }

    /**
     * （带非空检查）将 String 属性值，并转换成 Class<?>，且该 Class<?>
     *
     * @param literal 字符串
     * @param type    类型
     * @param <T>     T
     *
     * @return Class<?>
     */
    public static <T> T valueT(final String literal, final Class<?> type) {
        return TValue.vT(literal, type);
    }
}
