package io.modello.dynamic.modular.file.excel;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.cv.AoTable;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.plugin.excel.ExcelClient;
import io.vertx.up.plugin.excel.ExcelInfix;
import io.vertx.up.plugin.excel.atom.ExRecord;
import io.vertx.up.plugin.excel.atom.ExTable;
import io.vertx.up.uca.log.DevEnv;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Excel 模型分析器，用于分析Excel模板
 * 1. 提取所有的 Entity 部分生成 模型集合
 * 2. 包括：Entity, Field, Key, Index
 */
public class ExModello {

    private static final ExcelClient CLIENT = ExcelInfix.getClient();
    private transient final ConcurrentMap<String, Set<ExRecord>> recordMap
        = new ConcurrentHashMap<>();
    /* 应用程序名称 */
    private transient String appName;

    private ExModello(final Set<String> files) {
        files.forEach(file -> {
            try {
                final Set<ExTable> tables = CLIENT.ingest(file);
                this.initMap(tables);
            } catch (final Throwable ex) {
                if (DevEnv.devJvmStack()) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static ExModello create(final Set<String> files) {
        return new ExModello(files);
    }

    public ExModello on(final String appName) {
        /* 计算名空间 */
        this.appName = appName;
        return this;
    }

    public Set<Schema> build() {
        /*
         * 查找所有的 M_ENTITY
         */
        final Set<Schema> schemaSet = new HashSet<>();
        /*
         * 先根据 ExRecord 记录构造 schema
         */
        final Set<ExRecord> entities = this.get(AoTable.ENTITY);

        entities.forEach(entity -> {
            /*
             * 根据 schema 查找 fields, keys, indexes
             */
            final Set<ExRecord> fields = ExIn.searchEntity(this.get(AoTable.FIELD), entity);
            final Set<ExRecord> keys = ExIn.searchEntity(this.get(AoTable.KEY), entity);
            final Set<ExRecord> indexes = ExIn.searchEntity(this.get(AoTable.INDEX), entity);
            /*
             * 数据收集完成
             */
            final JsonObject schemaJson = ExOut.toSchema(entity, fields, keys, indexes);
            schemaSet.add(Ao.toSchema(this.appName, schemaJson));
        });
        return schemaSet;
    }

    private Set<ExRecord> get(final String key) {
        return this.recordMap.getOrDefault(key, new HashSet<>());
    }

    private void initMap(final Set<ExTable> tables) {

        final List<String> from = new ArrayList<String>() {
            {
                this.add(AoTable.ENTITY);
                this.add(AoTable.FIELD);
                this.add(AoTable.KEY);
                this.add(AoTable.INDEX);
            }
        };
        final List<Set<ExRecord>> to = new ArrayList<Set<ExRecord>>() {
            {
                this.add(ExIn.record(tables, AoTable.ENTITY));
                this.add(ExIn.record(tables, AoTable.FIELD));
                this.add(ExIn.record(tables, AoTable.KEY));
                this.add(ExIn.record(tables, AoTable.INDEX));
            }
        };
        /*
         * 因为存在多个表，所以此处需要处理
         */
        Ut.elementZip(from, to).forEach((key, records) -> {
            if (this.recordMap.containsKey(key)) {
                this.recordMap.get(key).addAll(records);
            } else {
                this.recordMap.put(key, records);
            }
        });
    }
}
