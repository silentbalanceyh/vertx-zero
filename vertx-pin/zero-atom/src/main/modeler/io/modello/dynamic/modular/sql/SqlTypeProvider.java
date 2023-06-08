package io.modello.dynamic.modular.sql;

import io.horizon.uca.cache.Cc;
import io.modello.atom.app.KDatabase;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 读取配置文件处理Sql的类型信息
 */
public class SqlTypeProvider {
    private static final ConcurrentMap<String, String> DB_MAPPING =
        new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, JsonArray> DB_TYPE_MAPPING =
        new ConcurrentHashMap<>();
    private static final Cc<String, SqlTypeProvider> CC_PROVIDER = Cc.open();

    private SqlTypeProvider(final KDatabase database) {
        final JsonObject schemaData = Ut.ioJObject(
            "engine/database/sql/" + database.getCategory().name() + "/schema.json");
        final JsonObject definitions = schemaData.getJsonObject("definitions");
        for (final String field : definitions.fieldNames()) {
            if (Ut.isNotNil(field) && null != definitions.getValue(field)) {
                DB_MAPPING.put(field, definitions.getString(field));
            }
        }
        final JsonObject typeMappings = schemaData.getJsonObject("mappings");
        for (final String field : typeMappings.fieldNames()) {
            if (Ut.isNotNil(field) && null != typeMappings.getValue(field)) {
                DB_TYPE_MAPPING.put(field, typeMappings.getJsonArray(field));
            }
        }
    }

    public static SqlTypeProvider create(final KDatabase database) {
        return CC_PROVIDER.pick(() -> new SqlTypeProvider(database), database.getCategory().name());
        // Fn.po?l(DB_TYPE_REF, database.getCategory().name(), () -> new SqlTypeProvider(database));
    }

    public String getColumnType(final String key) {
        return DB_MAPPING.get(key);
    }

    public JsonArray getMappingList(final String key) {
        return DB_TYPE_MAPPING.get(key);
    }
}
