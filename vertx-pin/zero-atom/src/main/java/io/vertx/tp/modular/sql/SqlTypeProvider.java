package io.vertx.tp.modular.sql;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Database;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

/**
 * 读取配置文件处理Sql的类型信息
 */
public class SqlTypeProvider {

    private SqlTypeProvider(final Database database) {
        final JsonObject schemaData = Ut.ioJObject(
            "engine/database/sql/" + database.getCategory().name() + "/schema.json");
        final JsonObject definitions = schemaData.getJsonObject("definitions");
        for (final String field : definitions.fieldNames()) {
            if (Ut.notNil(field) && null != definitions.getValue(field)) {
                Pool.DB_MAPPING.put(field, definitions.getString(field));
            }
        }
        final JsonObject typeMappings = schemaData.getJsonObject("mappings");
        for (final String field : typeMappings.fieldNames()) {
            if (Ut.notNil(field) && null != typeMappings.getValue(field)) {
                Pool.DB_TYPE_MAPPING.put(field, typeMappings.getJsonArray(field));
            }
        }
    }

    public static SqlTypeProvider create(final Database database) {
        return Fn.pool(Pool.DB_TYPE_REF, database.getCategory().name(),
            () -> new SqlTypeProvider(database));
    }

    public String getColumnType(final String key) {
        return Pool.DB_MAPPING.get(key);
    }

    public JsonArray getMappingList(final String key) {
        return Pool.DB_TYPE_MAPPING.get(key);
    }
}
