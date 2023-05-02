package io.horizon.util;

import io.horizon.atom.common.Metadata;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author lang : 2023/4/30
 */
class TValue {

    static JsonObject valueMerge(final JsonObject reference, final JsonObject source) {
        if (TIs.isNil(source)) {
            return reference;
        }
        reference.mergeIn(source, true);
        return reference;
    }

    static <T> void valueMerge(final JsonObject input, final String field, final T value) {
        Objects.requireNonNull(field);
        final JsonObject inputJ = HJson.valueJObject(input, false);
        if (Objects.isNull(value)) {
            inputJ.putNull(field);
        } else {
            inputJ.put(field, value);
        }
    }

    static JsonObject valueAppend(final JsonObject target, final JsonObject source, final boolean isRef) {
        final JsonObject reference = isRef ? target : target.copy();
        source.fieldNames().stream()
            .filter(field -> !reference.containsKey(field))
            .forEach(field -> reference.put(field, source.getValue(field)));
        return reference;
    }

    static JsonObject valueCopy(final JsonObject record, final String from, final String to) {
        if (Objects.isNull(record) || TIs.isNil(to)) {
            return null;
        }
        final Object value = record.getValue(from);
        if (Objects.nonNull(value)) {
            record.put(to, value);
        }
        return record;
    }

    static JsonObject valueDefault(final JsonObject record, final String field, final Object value) {
        if (Objects.isNull(record)) {
            // 如果 record 为空，返回一个空的 JsonObject
            return new JsonObject();
        }
        if (TIs.isNil(field)) {
            // 如果 field 为空，则直接跳过，返回 record
            return record;
        }
        final Object valueOriginal = record.getValue(field);
        if (Objects.isNull(valueOriginal)) {
            // 此处只检查 null
            record.put(field, value);
        }
        return record;
    }

    static JsonObject valueCopy(final JsonObject target, final JsonObject source, final String... fields) {
        Objects.requireNonNull(target);
        final JsonObject sourceJ = HJson.valueJObject(source, false);
        if (TIs.isNil(sourceJ)) {
            return target;
        }
        Arrays.stream(fields).forEach(field -> {
            final Object value = source.getValue(field);
            if (Objects.nonNull(value)) {
                target.put(field, value);
            }
        });
        return target;
    }

    // -------------------- 属性转换类方法 --------------------


    static void valueToMerge(final JsonObject input, final JsonObject value) {
        final JsonObject inputJ = HJson.valueJObject(input, false);
        if (!TIs.isNil(value)) {
            inputJ.mergeIn(value, true);
        }
    }

    static void valueToString(final JsonObject json, final String field) {
        if (TIs.isNil(json)) {
            return;
        }
        final Object value = json.getValue(field);
        if (Objects.isNull(value)) {
            return;
        }
        if (value instanceof JsonObject) {
            // JsonObject -> String
            final String literal = ((JsonObject) value).encode();
            json.put(field, literal);
        } else if (value instanceof JsonArray) {
            // JsonArray -> String
            final String literal = ((JsonArray) value).encode();
            json.put(field, literal);
        }
    }

    /*
     * field:  String
     * to:     JsonObject | JsonArray
     */
    static void valueToJson(final ClusterSerializable json, final String field) {
        if (json instanceof final JsonArray array) {
            HIter.itJArray(array).forEach(item -> valueToJson(item, field));
        } else if (json instanceof final JsonObject object) {
            if (HaS.isNotNil(object)) {
                final Object value = object.getValue(field);
                if (value instanceof final String literal) {
                    // String Literal
                    if (HaS.isJObject(literal)) {
                        final JsonObject replaced = HaS.toJObject(literal, TValue::valueToMetadata);
                        object.put(field, replaced);
                    } else if (HaS.isJArray(literal)) {
                        final JsonArray replaced = HaS.toJArray(literal, TValue::valueToMetadata);
                        object.put(field, replaced);
                    }
                } else if (value instanceof final JsonObject valueJ) {
                    // JsonObject
                    object.put(field, valueToMetadata(valueJ));
                } else if (value instanceof final JsonArray valueA) {
                    // JsonArray
                    final JsonArray replaced = new JsonArray();
                    // Element Extracting
                    valueA.forEach(valueE -> {
                        if (valueE instanceof final JsonObject valueJ) {
                            // Element = JsonObject
                            replaced.add(valueToMetadata(valueJ));
                        } else if (valueE instanceof final String valueS) {
                            // Element = String
                            replaced.add(valueS);
                        } else if (valueE instanceof final JsonArray valueIA) {
                            // Element = JsonArray（Fix Issue）
                            replaced.add(valueIA);
                        }
                    });
                    object.put(field, replaced);
                }
            }
        }
    }

    private static JsonObject valueToMetadata(final JsonObject metadata) {
        assert Objects.nonNull(metadata) : "Here input metadata should not be null";
        /*
         * Structure that will be parsed here.
         */
        return new Metadata(metadata).toJson();
    }
}
