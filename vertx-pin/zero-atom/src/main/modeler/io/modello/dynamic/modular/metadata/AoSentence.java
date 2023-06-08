package io.modello.dynamic.modular.metadata;

import cn.vertxup.atom.domain.tables.pojos.MField;
import cn.vertxup.atom.domain.tables.pojos.MKey;
import io.vertx.core.json.JsonArray;
import io.vertx.mod.atom.cv.em.CheckResult;

import java.util.concurrent.ConcurrentMap;

/**
 * 语句构造器专用接口，用于构造不同的SQL语句专用组件，且不同的数据库构造SQL语句的方式不一样
 */
public interface AoSentence {
    /* 构造字段行 */
    String segmentField(
        MField field);

    /* 普通PK, UK */
    String segmentKey(
        MKey key);

    /* 表是否存在 */
    String expectTable(
        String tableName);

    /* 修改约束 */
    String constraintDrop(
        String tableName, String constraintName);

    String constraintAdd(
        String tableName, MKey key);

    /* 修改列 */
    String columnDrop(String tableName, String columnName);

    String columnDropRename(String tableName, String columnName, String newColumnName, String fieldType);

    String columnAdd(String tableName, MField field);

    String columnAlter(String tableName, MField field);

    String columnDdl(String columnName);

    String columnType(MField field);

    JsonArray mappingList(final String key);

    CheckResult checkFieldType(MField field, ConcurrentMap<String, Object> columnDetail);
}
