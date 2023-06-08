package io.horizon.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author lang : 2023-05-09
 */
@SuppressWarnings("all")
class _Serialize extends _Reflect {
    protected _Serialize() {
    }

    /**
     * 将传入对象序列化成 String，执行Json序列化
     *
     * @param t   传入对象
     * @param <T> 泛型
     *
     * @return 序列化后的字符串
     */
    public static <T> String serialize(final T t) {
        return HJackson.serialize(t);
    }

    /**
     * 将传入对象序列化成 JsonObject / JsonArray，执行Json序列化
     *
     * @param t   传入对象
     * @param <T> 输入类型
     * @param <R> 返回类型
     *
     * @return 序列化好的对象
     */
    public static <T, R extends Iterable> R serializeJson(final T t) {
        return HJackson.serializeJson(t);
    }

    /**
     * 返回 Jackson 中原生的 Mapper
     *
     * @return JsonMapper
     */
    public static JsonMapper jacksonMapper() {
        return HJackson.mapper();
    }

    /**
     * 将传入的 JsonObject / JsonArray 反序列化成指定类型
     *
     * @param value JsonObject / JsonArray
     * @param type  指定类型
     * @param <T>   泛型
     *
     * @return 反序列化后的对象
     */
    public static <T> T deserialize(final JsonObject value, final Class<T> type) {
        return HJackson.deserialize(value, type);
    }

    /**
     * 将传入的 JsonObject / JsonArray 反序列化成指定类型
     *
     * @param value JsonObject / JsonArray
     * @param type  指定类型
     * @param <T>   泛型
     *
     * @return 反序列化后的对象
     */
    public static <T> T deserialize(final JsonArray value, final Class<T> type) {
        return HJackson.deserialize(value, type);
    }

    /**
     * 将传入的 JsonObject / JsonArray 反序列化成指定类型
     *
     * @param value JsonObject / JsonArray
     * @param type  指定类型
     * @param <T>   泛型
     *
     * @return 反序列化后的对象
     */
    public static <T> List<T> deserialize(final JsonArray value, final TypeReference<List<T>> type) {
        return HJackson.deserialize(value, type);
    }

    /**
     * 将传入的 String 反序列化成指定类型
     *
     * @param value String
     * @param type  指定类型
     * @param <T>   泛型
     *
     * @return 反序列化后的对象
     */
    public static <T> T deserialize(final String value, final TypeReference<T> type) {
        return HJackson.deserialize(value, type);
    }

    /**
     * 将传入的 String 反序列化成指定类型
     *
     * @param value String
     * @param type  指定类型
     * @param <T>   泛型
     *
     * @return 反序列化后的对象
     */
    public static <T> T deserialize(final String value, final Class<T> type) {
        return HJackson.deserialize(value, type);
    }
}
