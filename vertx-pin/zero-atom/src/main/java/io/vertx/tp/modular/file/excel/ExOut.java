package io.vertx.tp.modular.file.excel;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.plugin.excel.atom.ExRecord;

import java.util.Objects;
import java.util.Set;

/*
 * 将 ExTable 转换成 Json
 */
class ExOut {

    static JsonObject toModel(final ExRecord record,
                              final Set<ExRecord> joins,
                              final Set<ExRecord> attributes) {
        final JsonObject result = new JsonObject();
        result.put(KeField.MODEL, record.toJson());
        result.put(KeField.Modeling.JOINS, toJson(joins));
        result.put(KeField.Modeling.ATTRIBUTES, toJson(attributes));
        return result;
    }

    static JsonObject toSchema(final ExRecord record,
                               final Set<ExRecord> fields,
                               final Set<ExRecord> keys,
                               final Set<ExRecord> indexes) {
        final JsonObject result = new JsonObject();
        result.put(KeField.ENTITY, record.toJson());
        result.put(KeField.Modeling.FIELDS, toJson(fields));
        result.put(KeField.Modeling.KEYS, toJson(keys));
        result.put(KeField.Modeling.INDEXES, toJson(indexes));
        return result;
    }

    private static JsonArray toJson(final Set<ExRecord> records) {
        final JsonArray array = new JsonArray();
        records.stream().filter(Objects::nonNull)
                .map(ExRecord::toJson)
                .forEach(array::add);
        return array;
    }
}
