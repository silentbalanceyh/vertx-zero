package io.horizon.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lang : 2023/4/30
 */
class _Compare extends _Color {
    protected _Compare() {
    }

    /**
     * 检查两个日期是否相等
     *
     * @param left  左边日期
     * @param right 右边日期
     *
     * @return 是否相等
     */
    public static boolean isSame(final Date left, final Date right) {
        return TPeriod.isSame(left, right);
    }

    /**
     * 对象比较函数，比较两个对象内容是否相等，带 null 检查的版本
     *
     * @param left  左对象
     * @param right 右对象
     *
     * @return 是否相等
     */
    public static boolean isSame(final Object left, final Object right) {
        return TIs.isSame(left, right);
    }

    /**
     * 检查JsonObject中 field 属性的值是否和期望值相同，若JsonObject 为空，直接返回false
     *
     * @param record   JsonObject
     * @param field    字段
     * @param expected 期望值
     * @param <T>      期望值类型
     *
     * @return 是否相同
     */
    public static <T> boolean isSame(final JsonObject record, final String field, final T expected) {
        return HJson.isSame(record, field, expected);
    }

    /**
     * 比对两个 JsonArray 中的 fields 属性集合是否相同
     * sequence = false，表示不考虑顺序
     *
     * @param left     左边JsonArray
     * @param right    右边JsonArray
     * @param fields   比较的属性集合
     * @param sequence 是否考虑顺序
     *
     * @return 是否相同
     */
    public static boolean isSame(final JsonArray left, final JsonArray right,
                                 final Set<String> fields, final boolean sequence) {
        return HJson.isSame(left, right, fields, sequence);
    }

    /**
     * 比对两个 JsonArray 中的 fields 属性集合是否相同
     * sequence = false，默认表示不考虑顺序
     *
     * @param left   左边JsonArray
     * @param right  右边JsonArray
     * @param fields 比较的属性集合
     *
     * @return 是否相同
     */
    public static boolean isSame(final JsonArray left, final JsonArray right,
                                 final Set<String> fields) {
        return HJson.isSame(left, right, fields, false);
    }

    /**
     * 比对两个 JsonArray 中的 fields 属性集合是否相同
     * sequence = false，默认表示不考虑顺序
     *
     * @param left  左边JsonArray
     * @param right 右边JsonArray
     *
     * @return 是否相同
     */
    public static boolean isSame(final JsonArray left, final JsonArray right) {
        return HJson.isSame(left, right, new HashSet<>(), false);
    }

    /**
     * 比对两个 JsonArray 中的 fields 属性集合是否不同
     * sequence = false，表示不考虑顺序
     *
     * @param left     左边JsonArray
     * @param right    右边JsonArray
     * @param fields   比较的属性集合
     * @param sequence 是否考虑顺序
     *
     * @return 是否不同
     */
    public static boolean isDiff(final JsonArray left, final JsonArray right,
                                 final Set<String> fields, final boolean sequence) {
        return !HJson.isSame(left, right, fields, sequence);
    }

    /**
     * 比对两个 JsonArray 中的 fields 属性集合是否不同
     * sequence = false，默认表示不考虑顺序
     *
     * @param left   左边JsonArray
     * @param right  右边JsonArray
     * @param fields 比较的属性集合
     *
     * @return 是否不同
     */
    public static boolean isDiff(final JsonArray left, final JsonArray right,
                                 final Set<String> fields) {
        return !HJson.isSame(left, right, fields, false);
    }

    /**
     * 比对两个 JsonArray 中的 fields 属性集合是否不同
     * sequence = false，默认表示不考虑顺序
     *
     * @param left  左边JsonArray
     * @param right 右边JsonArray
     *
     * @return 是否不同
     */
    public static boolean isDiff(final JsonArray left, final JsonArray right) {
        return !HJson.isSame(left, right, new HashSet<>(), false);
    }
}
