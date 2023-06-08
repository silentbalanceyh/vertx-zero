package io.modello.dynamic.modular.file.excel;

import io.vertx.up.eon.KName;
import io.vertx.up.plugin.excel.atom.ExRecord;
import io.vertx.up.plugin.excel.atom.ExTable;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static io.vertx.mod.atom.refine.Ao.LOG;

/*
 * 针对 Set<ExTable> 的处理
 */
class ExIn {
    /*
     * 将 ExTable 转换成 ExRecord
     */
    static Set<ExRecord> record(final Set<ExTable> tables,
                                final String tableName) {
        return tables.stream()
            /* 直接过滤筛选所有符合条件的 ExTable */
            .filter(table -> tableName.equals(table.getName()))
            /* 拉平所有的表读取记录集 */
            .flatMap(table -> table.get().stream())
            .collect(Collectors.toSet());
    }

    /*
     * 根据 Entity 查找 Field, Index, Key
     * records:
     * - M_FIELD
     * - M_KEY
     * - M_INDEX
     * （带 entityId 的表都可以用这个方法）
     */
    static Set<ExRecord> searchEntity(final Set<ExRecord> records, final ExRecord record) {
        final String key = record.get(KName.KEY);
        return records.stream()
            .filter(each -> key.equals(each.get(KName.ENTITY_ID)))
            .collect(Collectors.toSet());
    }

    /*
     * 根据 Model 查找 Joins
     * records: M_JOIN 表
     */
    static Set<ExRecord> join(final Set<ExRecord> records, final ExRecord record) {
        /* 获取模型标识 identifier 和名空间 */
        final String identifier = record.get(KName.IDENTIFIER);
        final String namespace = record.get(KName.NAMESPACE);
        if (Ut.isNil(namespace)) {
            LOG.Atom.warn(ExIn.class, "数据记录中缺乏名空间: {0}", namespace);
        }
        /* 查找同一名空间下的关联关系 */
        return records.stream()
            /* model = identifier */
            .filter(each -> identifier.equals(each.get(KName.MODEL)))
            /* namespace = namespace */
            .filter(each -> namespace.equals(each.get(KName.NAMESPACE)))
            .collect(Collectors.toSet());
    }

    /*
     * 根据 Model 查找 Attribute
     * records: M_ATTRIBUTE 表
     * （带 modelId 的表都可以用这个方法）
     */
    static Set<ExRecord> searchModel(final Set<ExRecord> records, final ExRecord record) {
        final String key = record.get(KName.KEY);
        return records.stream()
            .filter(each -> key.equals(each.get(KName.MODEL_ID)))
            .collect(Collectors.toSet());
    }

    /*
     * 根据 Joins 查找 Schema 的记录集合
     */
    static Set<ExRecord> schemata(final Set<ExRecord> records, final Set<ExRecord> joins) {
        final Set<ExRecord> schemaSet = new HashSet<>();
        joins.forEach(join -> {
            /* 1. 读取 Schema */
            final String namespace = join.get(KName.NAMESPACE);
            final String identifier = join.get(KName.ENTITY);
            /* 2. 过滤唯一的 ExRecord */
            final ExRecord record = records.stream()
                .filter(each -> namespace.equals(each.get(KName.NAMESPACE)))
                .filter(each -> identifier.equals(each.get(KName.IDENTIFIER)))
                .findFirst().orElse(null);
            if (Objects.nonNull(record)) {
                schemaSet.add(record);
            }
        });
        return schemaSet;
    }
}
