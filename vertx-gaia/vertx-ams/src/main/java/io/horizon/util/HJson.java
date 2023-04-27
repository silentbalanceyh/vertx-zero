package io.horizon.util;

import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

/**
 * @author lang : 2023/4/27
 */
class HJson {
    private HJson() {
    }

    static boolean isJArray(final Object value) {
        return Objects.nonNull(value)
            && isJArray(value.toString());
    }

    static boolean isJArray(final String literal) {
        if (HIs.isNil(literal)) {
            return false;
        }
        try {
            Json.CODEC.fromString(literal, JsonArray.class);
            return true;
        } catch (final DecodeException ex) {
            return false;
        }
    }

    static boolean isJObject(final Object value) {
        return Objects.nonNull(value)
            && isJObject(value.toString());
    }

    static boolean isJObject(final String literal) {
        if (HIs.isNil(literal)) {
            return false;
        }
        try {
            Json.CODEC.fromString(literal, JsonObject.class);
            return true;
        } catch (final DecodeException ex) {
            return false;
        }
    }

    static JsonObject valueJObject(final JsonObject input, final String field) {
        if (HIs.isNil(field) || HIs.isNil(input)) {
            return new JsonObject();
        }
        final Object value = input.getValue(field);
        if (Objects.isNull(value)) {
            return new JsonObject();
        }
        if (value instanceof JsonObject) {
            return (JsonObject) value;
        } else {
            // #LOGGING
            return new JsonObject();
        }
    }

    static JsonArray valueJArray(final JsonObject input, final String field) {
        if (HIs.isNil(field) || HIs.isNil(input)) {
            return new JsonArray();
        }
        final Object value = input.getValue(field);
        if (Objects.isNull(value)) {
            return new JsonArray();
        }
        if (value instanceof JsonArray) {
            return (JsonArray) value;
        } else {
            // #LOGGING
            return new JsonArray();
        }
    }

    // 安全提取的专用方法
    static JsonObject valueJObject(final JsonObject object, final boolean isCopy) {
        if (HIs.isNil(object)) {
            return new JsonObject();
        }
        if (isCopy) {
            // 返回副本
            return object.copy();
        } else {
            // 返回拷贝
            return object;
        }
    }

    static JsonArray valueJArray(final JsonArray input, final boolean isCopy) {
        if (HIs.isNil(input)) {
            return new JsonArray();
        }
        if (isCopy) {
            // 返回副本
            return input.copy();
        } else {
            // 返回拷贝
            return input;
        }
    }
}
