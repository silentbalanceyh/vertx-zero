package io.modello.dynamic.modular.apply;

import io.vertx.core.json.JsonObject;

class SchemaDefault implements AoDefault {

    @Override
    public void applyJson(final JsonObject source) {
       /*
        final JsonObject entity = source.getJsonObject(KeField.ENTITY);
        // 默认追加Key相关定义
        // PrimaryKey = KEY
        final JsonArray keys = source.getJsonArray(PtField.KEYS);
        long PKCounter = Ut.itJArray(keys)
                .filter(item -> "PRIMARY".equals(item.getString("type")))
                .count();
        // 无主键追加默认主键
        if (0L == PKCounter) {
            // 主键补充
            keys.add(new JsonObject()
                    .put("type", "PRIMARY")
                    .put("columns", new JsonArray().add("KEY"))
                    .put("name", "PKey")
                    .put(KeField.ENTITY_ID, entity.getString(KeField.KEY))
                    .put(KeField.SIGMA, entity.getString(KeField.SIGMA))
                    .put(KeField.ACTIVE, true)
            );
        }
        // PrimaryKey 在 fields 中的定义
        final JsonArray fields = source.getJsonArray(PtField.FIELDS);
        long KeyCounter = Ut.itJArray(fields)
                .filter(item -> "key".equals(item.getString("name")))
                .count();
        // 没有主键，并且没有key字段时
        if (0L == PKCounter && 0L == KeyCounter) {
            fields.add(new JsonObject()
                    .put("columnName", "key")
                    .put("name", "key")
                    .put("columnType", "STRING1")
                    .put(KeField.TYPE, "java.lang.String")
                    .put("length", 36)
                    .put("isNullable", Boolean.FALSE)
                    .put("isPrimary", Boolean.TRUE)
                    .put(KeField.ENTITY_ID, entity.getString(KeField.KEY))
                    .put(KeField.SIGMA, entity.getString(KeField.SIGMA))
                    .put(KeField.ACTIVE, true)
            );
        }
        */
    }
}
