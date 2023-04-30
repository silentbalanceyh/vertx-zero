package io.horizon.util;

import io.horizon.eon.VValue;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.time.LocalDateTime;
import java.util.Arrays;
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
        return TV.vStringSet(array, field);
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
        return TV.vStringSet(list, executor);
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
        return TV.vArraySet(array, field);
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
        return TV.vString(array, field);
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
        return TV.vString(json, field, null);
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
        return TV.vString(json, field, defaultValue);
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
        return TV.vInt(json, field, VValue.RANGE);
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
        return TV.vClass(json, field, null);
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
        return TV.vClass(json, field, defaultClass);
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
        return TV.vClass(json, field, interfaceCls, null);
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
        return TV.vClass(json, field, interfaceCls, defaultClass);
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
        return TV.vT(literal, type);
    }

    /**
     * 从 JsonObject 数据中提取 field 属性的数据，并执行 type
     * 检查，若 type 匹配（==）则返回该数据，否则返回 null
     *
     * @param json  JsonObject
     * @param field 字段名
     * @param type  类型
     * @param <T>   T
     *
     * @return T
     */
    public static <T> T valueT(final JsonObject json, final String field, final Class<T> type) {
        return TV.vT(json, field, type);
    }

    /**
     * 「副作用方法」
     * 从 source 的 JsonObject 中拷贝 fields 属性相关信息到 target，该方法会改写 target
     * 拷贝对应属性过来，如果 source 中不存在该属性，则不拷贝
     *
     * @param target 目标
     * @param source 源
     * @param fields 字段
     *
     * @return JsonObject
     */
    public static JsonObject valueCopy(final JsonObject target, final JsonObject source, final String... fields) {
        return TValue.valueCopy(target, source, fields);
    }

    /**
     * 「副作用方法」
     * 将 record （ JsonObject 对象 ）本身的 from 属性拷贝到 to 属性
     *
     * @param record JsonObject
     * @param from   from
     * @param to     to
     *
     * @return JsonObject
     */
    public static JsonObject valueCopy(final JsonObject record, final String from, final String to) {
        return TValue.valueCopy(record, from, to);
    }

    /**
     * 将 sources 中的属性逐渐追加到 target 中，如果 target 中已经存在该属性，则不追加
     *
     * @param target  目标
     * @param sources 源
     *
     * @return JsonObject
     */
    public static JsonObject valueAppend(final JsonObject target, final JsonObject... sources) {
        Arrays.stream(sources).forEach(source -> TValue.valueAppend(target, source, true));
        return target;
    }

    /**
     * 使用 sources 中的属性逐渐覆盖 target 中的属性，直接覆盖，类似前端的 Object.assign
     *
     * @param target  目标
     * @param sources 源
     *
     * @return JsonObject
     */
    public static JsonObject valueMerge(final JsonObject target, final JsonObject... sources) {
        Arrays.stream(sources).forEach(source -> TValue.valueMerge(target, source, true));
        return target;
    }

    /**
     * 为 record （JsonObject类型）的对象追加默认值，如果 record 中的属性已经有值且值不为 null 时则追加
     * 1. 如果追加的 value 是 null 也会生效
     * 2. field 为空则跳过，record 为空则是 valueJObject 效果（返回 JsonObject 对象）
     * 3. 如果是 String 类型，只检查 null，不检查空字符串
     *
     * @param record JsonObject
     * @param field  字段名
     * @param value  默认值
     *
     * @return JsonObject
     */
    public static JsonObject valueDefault(final JsonObject record, final String field, final Object value) {
        return TValue.valueDefault(record, field, value);
    }

    /**
     * 为 record （JsonObject类型）的对象追加默认值，如果 record 中的属性已经有值且值不为 null 时则追加
     * 1. 如果追加的 value 是 null 也会生效
     * 2. field 为空则跳过，record 为空则是 valueJObject 效果（返回 JsonObject 对象）
     * 3. 如果是 String 类型，只检查 null，不检查空字符串
     *
     * @param record JsonObject
     * @param field  字段名
     *
     * @return JsonObject
     */
    public static JsonObject valueDefault(final JsonObject record, final String field) {
        return TValue.valueDefault(record, field, null);
    }

    // ------------- 原 if 系方法（同步模式使用新API）-------------

    /**
     * 「属性序列化/副作用」
     * 提取 JsonObject 记录中所有的 fields 属性，检查属性是否存在且不为空，若存在且不为空则
     * 检查该属性的值是否 JsonObject / JsonArray 类型，如果是这种类型，直接调用该类型的
     * encode() 方法，将这些属性转换成 String 格式
     *
     * @param json   JsonObject
     * @param fields 字段名
     *
     * @return JsonObject（修改过的）
     */
    public static JsonObject valueToString(final JsonObject json, final String... fields) {
        Arrays.stream(fields).forEach(field -> TValue.valueToString(json, field));
        return json;
    }

    /**
     * 「属性序列化/副作用」
     * 提取 JsonArray 记录中所有JsonObject对象的 fields 属性，检查属性是否存在且不为空，
     * 若存在且不为空则检查该属性的值是否 JsonObject / JsonArray 类型，如果是这种类型，
     * 直接调用该类型的encode() 方法，将这些属性转换成 String 格式（迭代执行）
     *
     * @param array  JsonArray
     * @param fields 字段名
     *
     * @return JsonArray（修改过的）
     */
    public static JsonArray valueToString(final JsonArray array, final String... fields) {
        HIter.itJArray(array).forEach(json -> valueToString(json, fields));
        return array;
    }

    /**
     * 「属性序列化/副作用」
     * 提取 JsonObject 记录中的所有 fields 属性相关信息，并执行智能化序列化
     * 1. String 格式包括：[] / {}
     * 2. 解析之后转换成 JsonObject / JsonArray
     * 3. 根对象：JsonObject / JsonArray
     * ---> JsonObject 属性递归
     * ---> JsonArray 属性递归
     *
     * @param json   JsonObject
     * @param fields 字段名
     *
     * @return JsonObject（修改过的）
     */
    public static JsonObject valueToJObject(final JsonObject json, final String... fields) {
        Arrays.stream(fields).forEach(field -> TValue.valueJson(json, field));
        return json;
    }


    /**
     * 「属性序列化/副作用」
     * 提取 JsonArray 记录中的所有 fields 属性相关信息，并执行智能化序列化
     * 1. String 格式包括：[] / {}
     * 2. 解析之后转换成 JsonObject / JsonArray
     * 3. 根对象：JsonObject / JsonArray
     * ---> JsonObject 属性递归
     * ---> JsonArray 属性递归
     *
     * @param array  JsonArray
     * @param fields 字段名
     *
     * @return JsonArray（修改过的）
     */
    public static JsonArray valueToJArray(final JsonArray array, final String... fields) {
        HIter.itJArray(array).forEach(json -> valueToJObject(json, fields));
        return array;
    }
}
