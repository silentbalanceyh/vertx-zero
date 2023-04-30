package io.horizon.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;
import java.util.function.Function;

/**
 * @author lang : 2023/4/28
 */
class _Element extends _EDS {
    /**
     * 针对数组执行拷贝，支持泛型数组的拷贝动作
     *
     * @param array   数组
     * @param element 元素
     * @param <T>     泛型
     *
     * @return 拷贝后的数组
     */
    public static <T> T[] elementAdd(final T[] array, final T element) {
        return CAdd.add(array, element);
    }

    /**
     * （引用修改）
     * 针对 JsonArray 对象执行按 field 匹配的添加操作
     * 直接修改当前 JsonArray 中 field 和传入对象匹配的方法，该操作属于
     * Save级，即可直接执行修改操作
     *
     * @param array JsonArray
     * @param json  JsonObject
     * @param field 字段名
     *
     * @return JsonArray
     */
    public static JsonArray elementSave(final JsonArray array, final JsonObject json, final String field) {
        return CSave.save(array, json, field);
    }

    /**
     * （引用修改）
     * 针对 JsonArray 对象执行按 field 匹配执行批量添加操作
     * 直接修改当前 JsonArray 中 field 和传入对象匹配的方法，该操作属于
     * Save级，即可直接执行修改操作，且添加过程中只针对 json 中的 JsonObject 合法对象
     *
     * @param array JsonArray
     * @param json  JsonObject
     * @param field 字段名
     *
     * @return JsonArray
     */
    public static JsonArray elementSave(final JsonArray array, final JsonArray json, final String field) {
        return CSave.save(array, json, field);
    }

    /**
     * （引用修改）
     * 针对 List<T> 集合执行按 field 匹配执行保存操作，反射提取 field 中的值
     *
     * @param list   集合
     * @param entity 实体
     * @param field  字段名
     * @param <T>    泛型
     *
     * @return List<T>
     */
    public static <T> List<T> elementSave(final List<T> list, final T entity, final String field) {
        return CSave.save(list, entity, item -> HInstance.get(item, field));
    }

    /**
     * （引用修改）
     * 针对 List<T> 集合执行按 field 匹配执行保存操作，反射提取 field 中的值
     *
     * @param list    集合
     * @param entity  实体
     * @param fieldFn 字段提取函数（非标准反射）
     * @param <T>     泛型
     *
     * @return List<T>
     */
    public static <T> List<T> elementSave(final List<T> list, final T entity, final Function<T, String> fieldFn) {
        return CSave.save(list, entity, fieldFn);
    }
    // ----------------------- 元素子集运算 ---------------------

    /**
     * 针对 JsonObject 对象按 fields 属性执行子集提取
     *
     * @param input  JsonObject
     * @param fields 字段名
     *
     * @return JsonObject
     */
    public static JsonObject elementSubset(final JsonObject input, final String... fields) {
        return CSubset.subset(input, new HashSet<>(Arrays.asList(fields))); // ArrayL.subset(input, fields);
    }

    /**
     * 针对 JsonObject 对象按 fields 属性执行子集提取
     *
     * @param input  JsonObject
     * @param fields 字段名
     *
     * @return JsonArray
     */
    public static JsonObject elementSubset(final JsonObject input, final Set<String> fields) {
        return CSubset.subset(input, fields); // ArrayL.subset(input, set);
    }

    /**
     * 针对 JsonArray 对象按 fields 属性执行子集提取
     *
     * @param input  JsonArray
     * @param fields 字段名
     *
     * @return JsonArray
     */
    public static JsonArray elementSubset(final JsonArray input, final Set<String> fields) {
        return CSubset.subset(input, fields); // ArrayL.subset(input, set);
    }

    /**
     * 针对 JsonArray 对象按 fields 属性执行子集提取
     *
     * @param input   JsonArray
     * @param matchFn 专用执行函数
     *
     * @return JsonArray
     */
    public static JsonArray elementSubset(final JsonArray input, final Function<JsonObject, Boolean> matchFn) {
        return CSubset.subset(input, matchFn::apply); // ArrayL.subset(input, fnFilter);
    }

    // ------------------------ 集合基本交、并、差运算 ------------------------

    /**
     * 针对 Set 集合执行交集运算
     *
     * @param left  左集合
     * @param right 右集合
     * @param <T>   泛型
     *
     * @return 左右集合交集
     */
    public static <T> Set<T> elementIntersect(final Set<T> left, final Set<T> right) {
        return CArithmetic.intersect(left, right);
    }

    /**
     * 针对 List 集合执行交集运算
     *
     * @param left  左集合
     * @param right 右集合
     * @param <T>   泛型
     *
     * @return 左右集合交集
     */
    public static <T> List<T> elementIntersect(final List<T> left, final List<T> right) {
        return new ArrayList<>(elementIntersect(new HashSet<>(left), new HashSet<>(right)));
    }

    /**
     * 针对 Set 集合执行交集运算，运算时按 fnGet 中提取的值执行基础规则，这种一般是非基础数据集合
     *
     * @param left  左集合
     * @param right 右集合
     * @param fnGet 专用执行函数
     * @param <T>   泛型
     * @param <V>   泛型
     *
     * @return 左右集合交集
     */
    public static <T, V> Set<T> elementIntersect(final Set<T> left, final Set<T> right, final Function<T, V> fnGet) {
        return CArithmetic.intersect(left, right, fnGet);
    }

    /**
     * 针对 List 集合执行交集运算，运算时按 fnGet 中提取的值执行基础规则，这种一般是非基础数据集合
     *
     * @param left  左集合
     * @param right 右集合
     * @param fnGet 专用执行函数
     * @param <T>   泛型
     * @param <V>   泛型
     *
     * @return 左右集合交集
     */
    public static <T, V> List<T> elementIntersect(final List<T> left, final List<T> right, final Function<T, V> fnGet) {
        return new ArrayList<>(elementIntersect(new HashSet<>(left), new HashSet<>(right), fnGet));
    }

    /**
     * 针对 Set 集合执行并集运算
     *
     * @param left  左集合
     * @param right 右集合
     * @param <T>   泛型
     *
     * @return 左右集合并集
     */
    public static <T> Set<T> elementUnion(final Set<T> left, final Set<T> right) {
        return CArithmetic.union(left, right);
    }

    /**
     * 针对 List 集合执行并集运算
     *
     * @param left  左集合
     * @param right 右集合
     * @param <T>   泛型
     *
     * @return 左右集合并集
     */
    public static <T> List<T> elementUnion(final List<T> left, final List<T> right) {
        return new ArrayList<>(elementUnion(new HashSet<>(left), new HashSet<>(right)));
    }

    /**
     * 针对 Set 集合执行并集运算，运算时按 fnGet 中提取的值执行基础规则，这种一般是非基础数据集合
     *
     * @param left  左集合
     * @param right 右集合
     * @param fnGet 专用执行函数
     * @param <T>   泛型
     * @param <V>   泛型
     *
     * @return 左右集合并集
     */
    public static <T, V> Set<T> elementUnion(final Set<T> left, final Set<T> right, final Function<T, V> fnGet) {
        return CArithmetic.union(left, right, fnGet);
    }

    /**
     * 针对 List 集合执行并集运算，运算时按 fnGet 中提取的值执行基础规则，这种一般是非基础数据集合
     *
     * @param left  左集合
     * @param right 右集合
     * @param fnGet 专用执行函数
     * @param <T>   泛型
     * @param <V>   泛型
     *
     * @return 左右集合并集
     */
    public static <T, V> List<T> elementUnion(final List<T> left, final List<T> right, final Function<T, V> fnGet) {
        return new ArrayList<>(elementUnion(new HashSet<>(left), new HashSet<>(right), fnGet));
    }

    /**
     * 针对 Set 集合执行差集运算，注意这里是 subtrahend - minuend，有顺序
     *
     * @param subtrahend 被减数
     * @param minuend    减数
     * @param <T>        泛型
     *
     * @return 差集
     */
    public static <T> Set<T> elementDiff(final Set<T> subtrahend, final Set<T> minuend) {
        return CArithmetic.diff(subtrahend, minuend);
    }

    /**
     * 针对 List 集合执行差集运算，注意这里是 subtrahend - minuend，有顺序
     *
     * @param subtrahend 被减数
     * @param minuend    减数
     * @param <T>        泛型
     *
     * @return 差集
     */
    public static <T> List<T> elementDiff(final List<T> subtrahend, final List<T> minuend) {
        return new ArrayList<>(elementDiff(new HashSet<>(subtrahend), new HashSet<>(minuend)));
    }

    /**
     * 针对 Set 集合执行差集运算，注意这里是 subtrahend - minuend，有顺序
     * 运算时按 fnGet 中提取的值执行基础规则，这种一般是非基础数据集合
     *
     * @param subtrahend 被减数
     * @param minuend    减数
     * @param fnGet      专用执行函数
     * @param <T>        泛型
     * @param <V>        泛型
     *
     * @return 差集
     */
    public static <T, V> Set<T> elementDiff(final Set<T> subtrahend, final Set<T> minuend, final Function<T, V> fnGet) {
        return CArithmetic.diff(subtrahend, minuend, fnGet);
    }

    /**
     * 针对 List 集合执行差集运算，注意这里是 subtrahend - minuend，有顺序
     * 运算时按 fnGet 中提取的值执行基础规则，这种一般是非基础数据集合
     *
     * @param subtrahend 被减数
     * @param minuend    减数
     * @param fnGet      专用执行函数
     * @param <T>        泛型
     * @param <V>        泛型
     *
     * @return 差集
     */
    public static <T, V> List<T> elementDiff(final List<T> subtrahend, final List<T> minuend, final Function<T, V> fnGet) {
        return new ArrayList<>(elementDiff(new HashSet<>(subtrahend), new HashSet<>(minuend), fnGet));
    }
}
