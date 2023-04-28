package io.horizon.util;

import io.horizon.fn.HFn;
import io.horizon.specification.modeler.HRecord;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;
import java.util.function.Function;

/**
 * @author lang : 2023/4/27
 */
class HJson {
    private HJson() {
    }

    static boolean isJArray(final Object value) {
        // 只有 JsonArray 会去掉集合类型
        if (TType.isArray(value) || value instanceof Collection<?>) {
            return false;
        }
        return (value instanceof JsonArray)
            || (Objects.nonNull(value) && isJArray(value.toString()));
    }

    static boolean isJArray(final String literal) {
        if (TIs.isNil(literal)) {
            return false;
        }
        try {
            //            Json.CODEC.fromString(literal, JsonArray.class);
            new JsonArray(literal);
            return true;
        } catch (final DecodeException ex) {
            return false;
        }
    }

    static boolean isJObject(final Object value) {
        if (Objects.isNull(value)) {
            return false;
        }
        // 去掉 Map 类型
        if (LinkedHashMap.class.isAssignableFrom(value.getClass())) {
            return false;
        }
        return (value instanceof JsonObject)
            || isJObject(value.toString());
    }

    static boolean isJObject(final String literal) {
        if (TIs.isNil(literal)) {
            return false;
        }
        try {
            new JsonObject(literal);
            return true;
        } catch (final DecodeException ex) {
            return false;
        }
    }

    static JsonObject valueJObject(final JsonObject input, final String field) {
        if (TIs.isNil(field) || TIs.isNil(input)) {
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
        if (TIs.isNil(field) || TIs.isNil(input)) {
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
        if (TIs.isNil(object)) {
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
        if (TIs.isNil(input)) {
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


    static JsonObject toJObject(final Map<String, Object> map) {
        final JsonObject params = new JsonObject();
        HFn.runAt(() -> map.forEach(params::put), map);
        return params;
    }


    static JsonObject toJObject(final String literal) {
        if (TIs.isNil(literal)) {
            return new JsonObject();
        } else {
            try {
                return new JsonObject(literal);
            } catch (final DecodeException ex) {
                return new JsonObject();
            }
        }
    }

    static JsonArray toJArray(final HRecord[] records) {
        final JsonArray result = new JsonArray();
        if (Objects.nonNull(records)) {
            Arrays.stream(records).map(HRecord::toJson)
                .forEach(result::add);
        }
        return result;
    }

    static JsonArray toJArray(final JsonArray array, final Function<JsonObject, JsonObject> executor) {
        final JsonArray normalized = new JsonArray();
        HIter.itJArray(array).map(executor).forEach(normalized::add);
        return normalized;
    }

    static <T> JsonArray toJArray(final Set<T> set) {
        final JsonArray array = new JsonArray();
        if (Objects.nonNull(set)) {
            set.stream().filter(Objects::nonNull).forEach(array::add);
        }
        return array;
    }

    static <T> JsonArray toJArray(final List<T> list) {
        final JsonArray array = new JsonArray();
        if (Objects.nonNull(list)) {
            list.stream().filter(Objects::nonNull).forEach(array::add);
        }
        return array;
    }

    static JsonArray toJArray(final String literal) {
        if (TIs.isNil(literal)) {
            return new JsonArray();
        } else {
            try {
                return new JsonArray(literal);
            } catch (final DecodeException ex) {
                return new JsonArray();
            }
        }
    }

    static JsonArray toJArray(final String literal, final Function<JsonObject, JsonObject> itemFn) {
        final JsonArray parsed = toJArray(literal);
        if (Objects.isNull(itemFn)) {
            return parsed;
        } else {
            final JsonArray replaced = new JsonArray();
            parsed.forEach(item -> {
                if (item instanceof JsonObject) {
                    replaced.add(itemFn.apply((JsonObject) item));
                } else {
                    // Fix String Literal Serialization
                    replaced.add(item);
                }
            });
            // Ut.itJArray(parsed).map(itemFn).forEach(replaced::add);
            return replaced;
        }
    }

    static JsonObject toJObject(final Object value) {
        final JsonObject result = new JsonObject();
        HFn.runAt(() -> {
            if (isJObject(value)) {
                result.mergeIn((JsonObject) value, true);
            } else {
                result.mergeIn(toJObject(value.toString()), true);
            }
        }, value);
        return result;
    }

    static JsonArray toJArray(final Object value) {
        final JsonArray result = new JsonArray();
        HFn.runAt(() -> {
            if (isJArray(value)) {
                result.addAll((JsonArray) value);
            } else {
                final JsonArray direct = toJArray(value.toString());
                // Fix issue of ["[]"] String literal
                if (!direct.isEmpty()) {
                    result.addAll(direct);
                }
            }
        }, value);
        return result;
    }
}
