package io.modello.dynamic.modular.metadata;

import io.modello.eon.em.EmKey;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * 反向生成器
 * 当执行模型更新时，需要根据现有数据库中的元数据信息执行更新操作，所以需要反向读取数据库中的信息
 * 包括表、字段、键、约束、索引等。
 */
public interface AoReflector {
    /**
     * 获取当前表中所有约束信息
     */
    ConcurrentMap<String, EmKey.Type> getConstraints(String tableName);

    /**
     * 获取当前表中所有列信息
     */
    <T> List<T> getColumns(String tableName);

    /**
     * 获取当前列的详细信息
     */
    List<ConcurrentMap<String, Object>> getColumnDetail(String tableName);

    /**
     * 获取当前表中总列数
     */
    long getTotalRows(final String tableName);

    /**
     * 读取当前表中 column 为空的总列数
     */
    long getNullRows(final String tableName, final String column);

    String getFieldType(final ConcurrentMap<String, Object> columnDetail);

    ConcurrentMap<String, Object> getColumnDetails(final String column, final List<ConcurrentMap<String, Object>> columnDetailList);

    String getDataTypeWord();

    String getLengthWord();

}
