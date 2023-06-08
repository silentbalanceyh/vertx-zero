package io.modello.dynamic.modular.file.excel;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.plugin.excel.atom.ExRecord;

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
        result.put(KName.MODEL, record.toJson());
        result.put(KName.Modeling.JOINS, toJson(joins));
        result.put(KName.Modeling.ATTRIBUTES, toJson(attributes));
        return result;
    }

    static JsonObject toSchema(final ExRecord record,
                               final Set<ExRecord> fields,
                               final Set<ExRecord> keys,
                               final Set<ExRecord> indexes) {
        final JsonObject result = new JsonObject();
        result.put(KName.ENTITY, record.toJson());
        result.put(KName.Modeling.FIELDS, toJson(fields));
        result.put(KName.Modeling.KEYS, toJson(keys));
        result.put(KName.Modeling.INDEXES, toJson(indexes));
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
