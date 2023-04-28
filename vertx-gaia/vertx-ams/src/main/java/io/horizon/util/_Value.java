package io.horizon.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.time.LocalDateTime;
import java.util.Date;

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
}
