package io.vertx.up.fn;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.up.atom.config.Metadata;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.Objects;

/**
 * 原 Ut.ifX 方法，由于通常做的是函数连接，重构之后转移到 Fn.ifX 中
 * 这一系列的函数主要用于 compose 的流线型API中，可帮助我们完成更暴力的函数式异步开发
 * 摒弃掉 Future 模式后做防御式编排。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class If {
    /*
     * field:   JsonObject / JsonArray
     * to:      String ( encode() )
     */
    static void ifString(final JsonObject json, final String field) {
        if (Ut.isNotNil(json)) {
            final Object value = json.getValue(field);
            if (Objects.nonNull(value)) {
                if (value instanceof final JsonObject valueJ) {
                    // JsonObject -> String
                    final String literal = valueJ.encode();
                    json.put(field, literal);
                } else if (value instanceof final JsonArray valueA) {
                    // JsonArray -> String
                    final String literal = valueA.encode();
                    json.put(field, literal);
                }
            }
        }
    }

    /*
     * field:  String
     * to:     JsonObject | JsonArray
     */
    static void ifJson(final ClusterSerializable json, final String field) {
        if (json instanceof final JsonArray array) {
            Ut.itJArray(array).forEach(item -> ifJson(item, field));
        } else if (json instanceof final JsonObject object) {
            if (Ut.isNotNil(object)) {
                final Object value = object.getValue(field);
                if (value instanceof final String literal) {
                    // String Literal
                    if (Ut.isJObject(literal)) {
                        final JsonObject replaced = Ut.toJObject(literal, If::ifMetadata);
                        object.put(field, replaced);
                    } else if (Ut.isJArray(literal)) {
                        final JsonArray replaced = Ut.toJArray(literal, If::ifMetadata);
                        object.put(field, replaced);
                    }
                } else if (value instanceof final JsonObject valueJ) {
                    // JsonObject
                    object.put(field, If.ifMetadata(valueJ));
                } else if (value instanceof final JsonArray valueA) {
                    // JsonArray
                    final JsonArray replaced = new JsonArray();
                    // Element Extracting
                    valueA.forEach(valueE -> {
                        if (valueE instanceof final JsonObject valueJ) {
                            // Element = JsonObject
                            replaced.add(If.ifMetadata(valueJ));
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

    /*
     * page data: list + count
     */
    static JsonObject ifPage(final JsonObject pageData, final String... fields) {
        final JsonObject pageJ = Ut.valueJObject(pageData);
        if (pageJ.containsKey(KName.LIST) && pageJ.containsKey(KName.COUNT)) {
            final JsonArray listRef = Ut.valueJArray(pageJ, KName.LIST);
            Ut.itJArray(listRef).forEach(json -> Arrays.stream(fields).forEach(field -> ifJson(json, field)));
            pageJ.put(KName.LIST, listRef);
        }
        return pageJ;
    }

    static <T> JsonObject ifField(final JsonObject input, final String field, final T value) {
        final JsonObject inputJ = Ut.valueJObject(input);
        if (Ut.isNil(field)) {
            if (value instanceof final JsonObject data) {
                inputJ.mergeIn(data, true);
            }
        } else {
            inputJ.put(field, value);
        }
        return inputJ;
    }

    /*
     * ifCopy / ifCopies / ifDefault
     */
    static JsonObject ifCopy(final JsonObject record, final String from, final String to) {
        if (Objects.isNull(record) || Ut.isNil(to)) {
            return null;
        }
        final Object value = record.getValue(from);
        if (Objects.nonNull(value)) {
            record.put(to, value);
        }
        return record;
    }

    static JsonObject ifCopies(final JsonObject target, final JsonObject source, final String... fields) {
        Objects.requireNonNull(target);
        final JsonObject sourceJ = Ut.valueJObject(source);
        if (Ut.isNil(sourceJ)) {
            return target;
        }
        Arrays.stream(fields).forEach(field -> {
            if (sourceJ.containsKey(field)) {
                target.put(field, sourceJ.getValue(field));
            }
        });
        return target;
    }

    static JsonObject ifDefault(final JsonObject record, final String field, final Object value) {
        if (Objects.isNull(record)) {
            return null;
        }
        if (Ut.isNil(field)) {
            return record;
        }
        final Object valueOriginal = record.getValue(field);
        if (Objects.isNull(valueOriginal)) {
            record.put(field, value);
        }
        return record;
    }

    private static JsonObject ifMetadata(final JsonObject metadata) {
        assert Objects.nonNull(metadata) : "Here input metadata should not be null";
        /*
         * Structure that will be parsed here.
         */
        return new Metadata(metadata).toJson();
    }
}
